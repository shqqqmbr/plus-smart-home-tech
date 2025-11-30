package ru.practicum.handler.hub;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.practicum.constant.HubEventType;
import ru.practicum.handler.mapper.EnumMapper;
import ru.practicum.kafka.KafkaConfig;
import ru.yandex.practicum.grpc.telemetry.collector.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.collector.ScenarioAddedEventProto;
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
    public void handle(HubEventProto event) {
        ScenarioAddedEventProto ev = event.getScenarioAdded();
        List<ScenarioConditionAvro> conditions = ev.getConditionList().stream()
                .map(condition -> {
                    ScenarioConditionAvro.Builder builder = ScenarioConditionAvro.newBuilder()
                            .setSensorId(condition.getSensorId())
                            .setType(EnumMapper.map(condition.getType(), ConditionTypeAvro.class))
                            .setOperation(EnumMapper.map(condition.getOperation(), OperationTypeAvro.class));
                    builder.setValue(condition.getIntValue());
                    return builder.build();
                })
                .collect(Collectors.toList());

        List<DeviceActionAvro> actions = ev.getActionList().stream()
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
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(scenarioAddedEventAvro)
                .build();
        ProducerRecord<String, HubEventAvro> record = new ProducerRecord<>(
                kafkaConfig.getHubTopic(),
                hubEventAvro
        );
        kafkaProducer.send(record);
    }
}