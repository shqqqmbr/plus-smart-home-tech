package ru.practicum.handler.sensor;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.practicum.constant.SensorEventType;
import ru.practicum.handler.sensor.SensorEventHandler;
import ru.practicum.model.sensor.SensorEvent;
import ru.practicum.model.sensor.LightSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component(value = "LIGHT_SENSOR")
@AllArgsConstructor
public class LightSensorHandler implements SensorEventHandler {
    private final KafkaProducer<String, SensorEventAvro> kafkaProducer;

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.LIGHT_SENSOR;
    }

    @Override
    public void handle(SensorEvent event) {
        LightSensorEvent ev = (LightSensorEvent) event;

        LightSensorAvro lightSensorAvro = LightSensorAvro.newBuilder()
                .setLinkQuality(ev.getLinkQuality())
                .setLuminosity(ev.getLuminosity())
                .build();

        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(ev.getSensorId())
                .setHubId(ev.getHubId())
                .setTimestamp(ev.getTimestamp())
                .setPayload(lightSensorAvro)
                .build();

        ProducerRecord<String, SensorEventAvro> record = new ProducerRecord<>(
                "telemetry.sensors.v1",
                sensorEventAvro
        );
        kafkaProducer.send(record);
    }
}