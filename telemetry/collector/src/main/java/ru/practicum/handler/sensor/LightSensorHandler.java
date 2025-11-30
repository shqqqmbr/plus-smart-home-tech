package ru.practicum.handler.sensor;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.practicum.constant.SensorEventType;
import ru.practicum.kafka.KafkaConfig;
import ru.yandex.practicum.grpc.telemetry.collector.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.collector.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Component(value = "LIGHT_SENSOR")
@AllArgsConstructor
public class LightSensorHandler implements SensorEventHandler {
    private final KafkaProducer<String, SensorEventAvro> kafkaProducer;
    private final KafkaConfig kafkaConfig;

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {
        LightSensorProto ev = event.getLightSensor();
        LightSensorAvro lightSensorAvro = LightSensorAvro.newBuilder()
                .setLinkQuality(ev.getLinkQuality())
                .setLuminosity(ev.getLuminosity())
                .build();
        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds()))
                .setPayload(lightSensorAvro)
                .build();
        ProducerRecord<String, SensorEventAvro> record = new ProducerRecord<>(
                kafkaConfig.getSensorTopic(),
                sensorEventAvro
        );
        kafkaProducer.send(record);
    }
}