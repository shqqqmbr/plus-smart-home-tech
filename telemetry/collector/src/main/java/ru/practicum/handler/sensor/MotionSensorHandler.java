package ru.practicum.handler.sensor;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.practicum.constant.SensorEventType;
import ru.practicum.model.sensor.SensorEvent;
import ru.practicum.model.sensor.MotionSensorEvent;
import ru.practicum.telemetry.event.SensorEventAvro;
import ru.practicum.telemetry.event.MotionSensorAvro;

@Component(value = "MOTION_SENSOR")
@AllArgsConstructor
public class MotionSensorHandler implements SensorEventHandler {
    private final KafkaProducer<String, SensorEventAvro> kafkaProducer;

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.MOTION_SENSOR;
    }

    @Override
    public void handle(SensorEvent event) {
        MotionSensorEvent ev = (MotionSensorEvent) event;

        MotionSensorAvro motionSensorAvro = MotionSensorAvro.newBuilder()
                .setLinkQuality(ev.getLinkQuality())
                .setMotion(ev.getMotion())
                .setVoltage(ev.getVoltage())
                .build();

        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(ev.getSensorId())
                .setHubId(ev.getHubId())
                .setTimestamp(ev.getTimestamp())
                .setPayload(motionSensorAvro)
                .build();

        ProducerRecord<String, SensorEventAvro> record = new ProducerRecord<>(
                "telemetry.sensors.v1",
                sensorEventAvro
        );
        kafkaProducer.send(record);
    }
}