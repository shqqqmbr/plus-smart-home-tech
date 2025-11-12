package ru.practicum.handler.hub;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.practicum.constant.HubEventType;
import ru.practicum.handler.mapper.EnumMapper;
import ru.practicum.kafka.KafkaConfig;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.ScenarioAddedEvent;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;
import java.util.stream.Collectors;

@Component(value = "SCENARIO_ADDED")
@AllArgsConstructor
public class ScenarioAddedHandler implements HubEventHandler {
    private final KafkaProducer<String, HubEventAvro> kafkaProducer;
    private final KafkaConfig kafkaConfig;

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED_EVENT;
    }

    @Override
    public void handle(HubEvent event) {
        ScenarioAddedEvent ev = (ScenarioAddedEvent) event;
        List<ScenarioConditionAvro> conditions = ev.getConditions().stream()
                .map(condition -> {
                    ScenarioConditionAvro.Builder builder = ScenarioConditionAvro.newBuilder()
                            .setSensorId(condition.getSensorId())
                            .setType(EnumMapper.map(condition.getType(), ConditionTypeAvro.class))
                            .setOperation(EnumMapper.map(condition.getOperation(), OperationTypeAvro.class));
                    builder.setValue(condition.getValue());
                    return builder.build();
                })
                .collect(Collectors.toList());

        List<DeviceActionAvro> actions = ev.getActions().stream()
                .map(action -> {
                    DeviceActionAvro.Builder builder = DeviceActionAvro.newBuilder()
                            .setSensorId(action.getSensorId())
                            .setType(EnumMapper.map(action.getType(), ActionTypeAvro.class));
                    return builder.build();
                })
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
                kafkaConfig.getHubTopic(),
                hubEventAvro
        );
        kafkaProducer.send(record);
    }
}