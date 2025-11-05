package ru.practicum.handler.hub;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.practicum.constant.HubEventType;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.ScenarioRemovedEvent;
import ru.practicum.telemetry.event.HubEventAvro;
import ru.practicum.telemetry.event.ScenarioRemovedEventAvro;

@Component(value = "SCENARIO_REMOVED")
@AllArgsConstructor
public class ScenarioRemovedHandler implements HubEventHandler {
    private final KafkaProducer<String, HubEventAvro> kafkaProducer;

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_REMOVED;
    }

    @Override
    public void handle(HubEvent event) {
        ScenarioRemovedEvent ev = (ScenarioRemovedEvent) event;
        ScenarioRemovedEventAvro scenarioRemovedEventAvro = ScenarioRemovedEventAvro.newBuilder()
                .setName(ev.getName())
                .build();
        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(ev.getHubId())
                .setTimestamp(ev.getTimestamp())
                .setPayload(scenarioRemovedEventAvro)
                .build();
        ProducerRecord<String, HubEventAvro> record = new ProducerRecord<>(
                "telemetry.hubs.v1",
                hubEventAvro
        );
        kafkaProducer.send(record);
    }
}