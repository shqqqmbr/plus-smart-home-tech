package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.sensor.SensorEvent;
import ru.practicum.service.HubService;
import ru.practicum.service.SensorService;

@Slf4j
@RestController
@RequestMapping(path = "/events", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class EventsController {
    private final SensorService sensorService;
    private final HubService hubService;

    @PostMapping("/sensors")
    public void processSensorEvent(@RequestBody SensorEvent sensorEvent) {
        sensorService.processSensorEvent(sensorEvent);
        log.info("sensor event processed successfully from sensorId: {}", sensorEvent.getSensorId());

    }

    @PostMapping("/hubs")
    public void processHubEvent(@RequestBody HubEvent hubEvent) {
        hubService.processHubEvent(hubEvent);
        log.info("hub event processed successfully from hubId: {}", hubEvent.getHubId());
    }
}
