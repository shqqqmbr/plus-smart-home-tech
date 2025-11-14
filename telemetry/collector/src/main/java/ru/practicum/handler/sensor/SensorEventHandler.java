package ru.practicum.handler.sensor;

import ru.practicum.constant.SensorEventType;
import ru.practicum.model.sensor.SensorEvent;

public interface SensorEventHandler {
    SensorEventType getMessageType();

    void handle(SensorEvent event);
}
