package ru.practicum.handler.hub;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.practicum.constant.HubEventType;
import ru.practicum.handler.mapper.EnumMapper;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.ScenarioAddedEvent;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;
import java.util.stream.Collectors;

@Component(value = "SCENARIO_ADDED")
@AllArgsConstructor
public class ScenarioAddedHandler implements HubEventHandler {
    private final KafkaProducer<String, HubEventAvro> kafkaProducer;

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    public void handle(HubEvent event) {
        ScenarioAddedEvent ev = (ScenarioAddedEvent) event;

        List<ScenarioConditionAvro> conditions = ev.getConditions().stream()
                .map(condition -> {
                    ScenarioConditionAvro.Builder builder = ScenarioConditionAvro.newBuilder()
                            .setSensorId(condition.getSensorId())
                            .setType(EnumMapper.map(condition.getType(), ConditionTypeAvro.class))
                            .setOperation(EnumMapper.map(condition.getOperation(), ConditionOperationAvro.class));
                    Object value = condition.getValue();
                    if (value == null) {
                        builder = builder.setValue(null);
                    } else if (value instanceof Integer) {
                        builder = builder.setValue((Integer) value);
                    } else if (value instanceof Boolean) {
                        builder = builder.setValue((Boolean) value);
                    } else {
                        throw new IllegalArgumentException("Unsupported value type for condition: " + value.getClass());
                    }

                    return builder.build();
                })
                .collect(Collectors.toList());
        List<DeviceActionAvro> actions = ev.getActions().stream()
                .map(action -> DeviceActionAvro.newBuilder()
                        .setSensorId(action.getSensorId())
                        .setType(EnumMapper.map(action.getType(), ActionTypeAvro.class))
                        .setValue(action.getValue())
                        .build())
                .collect(Collectors.toList());
        ScenarioAddedEventAvro scenarioAddedEventAvro = ScenarioAddedEventAvro.newBuilder()
                .setName(ev.getName())
                .setConditions(conditions)
                .setActions(actions)
                .build();
        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(ev.getHubId())
                .setTimestamp(ev.getTimestamp())
                .setPayload(scenarioAddedEventAvro)
                .build();
        ProducerRecord<String, HubEventAvro> record = new ProducerRecord<>(
                "telemetry.hubs.v1",
                hubEventAvro
        );
        kafkaProducer.send(record);
    }

    private Object mapToUnionValue(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Integer) {
            return value;
        } else if (value instanceof Boolean) {
            return value;
        } else {
            throw new IllegalArgumentException("Unsupported value type: " + value.getClass());
        }
    }
}
