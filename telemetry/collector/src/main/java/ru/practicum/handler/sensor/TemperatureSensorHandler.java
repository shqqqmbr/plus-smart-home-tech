package ru.practicum.handler.sensor;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.practicum.constant.SensorEventType;
import ru.practicum.kafka.KafkaConfig;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

import java.time.Instant;

@Component(value = "TEMPERATURE_SENSOR")
@AllArgsConstructor
public class TemperatureSensorHandler implements SensorEventHandler {
    private final KafkaProducer<String, SensorEventAvro> kafkaSensorProducer;
    private final KafkaConfig kafkaConfig;

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {
        TemperatureSensorProto ev = event.getTemperatureSensor();
        TemperatureSensorAvro temperatureSensorAvro = TemperatureSensorAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds()))
                .setTemperatureC(ev.getTemperatureC())
                .setTemperatureF(ev.getTemperatureF())
                .build();
        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        event.getTimestamp().getSeconds(),
                        event.getTimestamp().getNanos()
                ))
                .setPayload(temperatureSensorAvro)
                .build();
        ProducerRecord<String, SensorEventAvro> record = new ProducerRecord<>(
                kafkaConfig.getSensorTopic(),
                sensorEventAvro
        );
        kafkaSensorProducer.send(record);
    }
}