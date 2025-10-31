package ru.practicum.handler.hub;

import ru.practicum.constant.HubEventType;
import ru.practicum.model.hub.HubEvent;

public interface HubEventHandler {
    HubEventType getMessageType();
    void handle(HubEvent event);
}
