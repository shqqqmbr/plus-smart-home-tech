package ru.practicum.handler.sensor;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.practicum.constant.SensorEventType;
import ru.practicum.model.sensor.ClimateSensorEvent;
import ru.practicum.model.sensor.SensorEvent;
import ru.practicum.telemetry.event.ClimateSensorAvro;
import ru.practicum.telemetry.event.SensorEventAvro;

@Component(value = "CLIMATE_SENSOR")
@AllArgsConstructor
public class ClimateSensorHandler implements SensorEventHandler {
    private final KafkaProducer<String, SensorEventAvro> kafkaProducer;

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.CLIMATE_SENSOR;
    }

    @Override
    public void handle(SensorEvent event) {
        ClimateSensorEvent ev = (ClimateSensorEvent) event;
        ClimateSensorAvro climateSensorAvro = ClimateSensorAvro.newBuilder()
                .setTemperatureC(ev.getTemperatureC())
                .setHumidity(ev.getHumidity())
                .setCo2Level(ev.getCo2level())
                .build();
        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(ev.getSensorId())
                .setHubId(ev.getHubId())
                .setTimestamp(ev.getTimestamp())
                .setPayload(climateSensorAvro)
                .build();
        ProducerRecord<String, SensorEventAvro> record = new ProducerRecord<>(
                "telemetry.sensors.v1",
                sensorEventAvro
        );
        kafkaProducer.send(record);
    }
}