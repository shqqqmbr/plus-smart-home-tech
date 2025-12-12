package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.model.Sensor;

@UtilityClass
public class SensorMapper {

    public Sensor avroToSensor(String hubId, DeviceAddedEventAvro deviceAddedEventAvro) {
        return Sensor.builder()
                .hubId(hubId)
                .sensorId(deviceAddedEventAvro.getId())
                .build();
    }

}
