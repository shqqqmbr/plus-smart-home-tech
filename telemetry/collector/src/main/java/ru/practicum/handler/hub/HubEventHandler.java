package ru.practicum.handler.hub;

import ru.practicum.constant.HubEventType;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;


public interface HubEventHandler {
    HubEventType getMessageType();

    void handle(HubEventProto event);
}
