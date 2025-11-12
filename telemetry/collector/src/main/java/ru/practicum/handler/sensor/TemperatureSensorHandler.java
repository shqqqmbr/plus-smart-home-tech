package ru.practicum.handler.sensor;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.practicum.constant.SensorEventType;
import ru.practicum.kafka.KafkaConfig;
import ru.practicum.model.sensor.SensorEvent;
import ru.practicum.model.sensor.TemperatureSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component(value = "TEMPERATURE_SENSOR")
@AllArgsConstructor
public class TemperatureSensorHandler implements SensorEventHandler {
    private final KafkaProducer<String, SensorEventAvro> kafkaProducer;
    private final KafkaConfig kafkaConfig;

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEvent event) {
        TemperatureSensorEvent ev = (TemperatureSensorEvent) event;
        TemperatureSensorAvro temperatureSensorAvro = TemperatureSensorAvro.newBuilder()
                .setId(ev.getSensorId())
                .setHubId(ev.getHubId())
                .setTimestamp(ev.getTimestamp())
                .setTemperatureC(ev.getTemperatureC())
                .setTemperatureF(ev.getTemperatureF())
                .build();
        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(ev.getSensorId())
                .setHubId(ev.getHubId())
                .setTimestamp(ev.getTimestamp())
                .setPayload(temperatureSensorAvro)
                .build();
        ProducerRecord<String, SensorEventAvro> record = new ProducerRecord<>(
                kafkaConfig.getSensorTopic(),
                sensorEventAvro
        );
        kafkaProducer.send(record);
    }
}