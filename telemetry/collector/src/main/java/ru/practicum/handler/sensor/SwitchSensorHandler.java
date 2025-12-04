package ru.practicum.handler.sensor;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.practicum.constant.SensorEventType;
import ru.practicum.kafka.KafkaConfig;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

import java.time.Instant;

@Component(value = "SWITCH_SENSOR")
@AllArgsConstructor
public class SwitchSensorHandler implements SensorEventHandler {
    private final KafkaProducer<String, SensorEventAvro> kafkaProducer;
    private final KafkaConfig kafkaConfig;

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {
        SwitchSensorProto ev = event.getSwitchSensor();
        SwitchSensorAvro switchSensorAvro = SwitchSensorAvro.newBuilder()
                .setState(ev.getState())
                .build();
        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        event.getTimestamp().getSeconds(),
                        event.getTimestamp().getNanos()
                ))
                .setPayload(switchSensorAvro)
                .build();
        ProducerRecord<String, SensorEventAvro> record = new ProducerRecord<>(
                kafkaConfig.getSensorTopic(),
                sensorEventAvro
        );
        kafkaProducer.send(record);
    }
}