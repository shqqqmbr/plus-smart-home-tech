package ru.practicum.handler.sensor;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.practicum.constant.SensorEventType;
import ru.practicum.handler.sensor.SensorEventHandler;
import ru.practicum.model.sensor.SensorEvent;
import ru.practicum.model.sensor.SwitchSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component(value = "SWITCH_SENSOR")
@AllArgsConstructor
public class SwitchSensorHandler implements SensorEventHandler {
    private final KafkaProducer<String, SensorEventAvro> kafkaProducer;

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.SWITCH_SENSOR;
    }

    @Override
    public void handle(SensorEvent event) {
        SwitchSensorEvent ev = (SwitchSensorEvent) event;

        SwitchSensorAvro switchSensorAvro = SwitchSensorAvro.newBuilder()
                .setState(ev.getState())
                .build();

        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(ev.getSensorId())
                .setHubId(ev.getHubId())
                .setTimestamp(ev.getTimestamp())
                .setPayload(switchSensorAvro)
                .build();

        ProducerRecord<String, SensorEventAvro> record = new ProducerRecord<>(
                "telemetry.sensors.v1",
                sensorEventAvro
        );
        kafkaProducer.send(record);
    }
}