package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.constant.HubEventType;
import ru.practicum.constant.SensorEventType;
import ru.practicum.handler.hub.HubEventHandler;
import ru.practicum.handler.sensor.SensorEventHandler;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.sensor.SensorEvent;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/events", consumes = MediaType.APPLICATION_JSON_VALUE)
public class EventsController {
    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlers;
    private final Map<HubEventType, HubEventHandler> hubEventHandlers;

    public EventsController(Set<SensorEventHandler> sensorEventHandlers, Set<HubEventHandler> hubEventHandlers) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
    }

    @PostMapping("/sensors")
    public void processSensorEvent(@RequestBody SensorEvent sensorEvent) {
        SensorEventHandler sensorEventHandler = sensorEventHandlers.get(sensorEvent.getType());
        if (sensorEventHandler == null) {
            throw new IllegalArgumentException("Sensor event type not found: " + sensorEvent.getType());
        }
        sensorEventHandler.handle(sensorEvent);
    }

    @PostMapping("/hubs")
    public void processHubEvent(@RequestBody HubEvent hubEvent) {
        HubEventHandler hubEventHandler = hubEventHandlers.get(hubEvent.getType());
        if (hubEventHandler == null) {
            throw new IllegalArgumentException("Hub event type not found: " + hubEvent.getType());
        }
        hubEventHandler.handle(hubEvent);
    }
}
