package ru.practicum.handler.sensor;

import ru.practicum.constant.SensorEventType;
import ru.yandex.practicum.grpc.telemetry.collector.SensorEventProto;

public interface SensorEventHandler {
    SensorEventType getMessageType();

    void handle(SensorEventProto event);
}
