package ru.practicum.controller;

import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.constant.HubEventType;
import ru.practicum.constant.SensorEventType;
import ru.practicum.handler.hub.HubEventHandler;
import ru.practicum.handler.sensor.SensorEventHandler;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.HubEventProtocol;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventProtocol;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;



@GrpcService
public class EventsController extends CollectorControllerGrpc.CollectorControllerImplBase {
    private final Map<SensorEventProtocol, SensorEventHandler> sensorEventHandlers;
    private final Map<HubEventProtocol, HubEventHandler> hubEventHandlers;

    public EventsController(Set<SensorEventHandler> sensorEventHandlers, Set<HubEventHandler> hubEventHandlers) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
    }

    public void processSensorEvent(@RequestBody SensorEvent sensorEvent) {
        SensorEventHandler sensorEventHandler = sensorEventHandlers.get(sensorEvent.getType());
        if (sensorEventHandler == null) {
            throw new IllegalArgumentException("Sensor event type not found: " + sensorEvent.getType());
        }
        sensorEventHandler.handle(sensorEvent);
    }

    public void processHubEvent(@RequestBody HubEvent hubEvent) {
        HubEventHandler hubEventHandler = hubEventHandlers.get(hubEvent.getType());
        if (hubEventHandler == null) {
            throw new IllegalArgumentException("Hub event type not found: " + hubEvent.getType());
        }
        hubEventHandler.handle(hubEvent);
    }
}