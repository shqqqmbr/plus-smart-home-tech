package ru.practicum.handler.sensor;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.practicum.constant.SensorEventType;
import ru.practicum.kafka.KafkaConfig;
import ru.yandex.practicum.grpc.telemetry.collector.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.collector.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Component(value = "CLIMATE_SENSOR")
@AllArgsConstructor
public class ClimateSensorHandler implements SensorEventHandler {
    private final KafkaProducer<String, SensorEventAvro> kafkaProducer;
    private final KafkaConfig kafkaConfig;

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {
        ClimateSensorProto ev = event.getClimateSensor();
        ClimateSensorAvro.Builder climateSensorBuilder = ClimateSensorAvro.newBuilder()
                .setTemperatureC(ev.getTemperatureC())
                .setHumidity(ev.getHumidity());
        Integer co2level = ev.getCo2Level();
        if (co2level != null) {
            climateSensorBuilder.setCo2Level(co2level);
        } else {
            climateSensorBuilder.setCo2Level(0);
        }
        ClimateSensorAvro climateSensorAvro = climateSensorBuilder.build();
        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds()))
                .setPayload(climateSensorAvro)
                .build();
        ProducerRecord<String, SensorEventAvro> record = new ProducerRecord<>(
                kafkaConfig.getSensorTopic(),
                sensorEventAvro
        );
        kafkaProducer.send(record);
    }
}