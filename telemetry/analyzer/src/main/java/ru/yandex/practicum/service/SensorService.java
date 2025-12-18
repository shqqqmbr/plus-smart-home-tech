package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Sensor;
import ru.yandex.practicum.repository.SensorRepository;

@Service
@RequiredArgsConstructor
public class SensorService {
    private final SensorRepository sensorRepository;

    public void add(Sensor sensor) {
        sensorRepository.save(sensor);
    }

    public void delete(String sensorId, String hubId) {
        sensorRepository.deleteBySensorIdAndHubId(sensorId, hubId);
    }
}
