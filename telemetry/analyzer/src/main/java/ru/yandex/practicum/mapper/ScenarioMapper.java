package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.model.*;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ScenarioMapper {

    public Scenario avroToScenario(String hubId, ScenarioAddedEventAvro scenarioAvro) {
        Set<ScenarioCondition> conditions = scenarioAvro.getConditions().stream()
                .map(ScenarioMapper::avroToScenarioCondition)
                .collect(Collectors.toSet());

        Set<Action> actions = scenarioAvro.getActions().stream()
                .map(ScenarioMapper::avroToAction)
                .collect(Collectors.toSet());

        return Scenario.builder()
                .name(scenarioAvro.getName())
                .hubId(hubId)
                .scenarioConditions(conditions)
                .scenarioActions(actions)
                .build();
    }

    private ScenarioCondition avroToScenarioCondition(ScenarioConditionAvro scenarioConditionAvro) {

        return ScenarioCondition.builder()
                .type(ConditionType.valueOf(scenarioConditionAvro.getType().name()))
                .operation(ConditionOperation.valueOf(scenarioConditionAvro.getOperation().name()))
                .value(valueToInteger(scenarioConditionAvro.getValue()))
                .sensorId(scenarioConditionAvro.getSensorId())
                .build();
    }

    private Action avroToAction(DeviceActionAvro actionAvro) {
        return Action.builder()
                .type(ActionType.valueOf(actionAvro.getType().name()))
                .value(actionAvro.getValue())
                .sensorId(actionAvro.getSensorId())
                .build();
    }

    private Integer valueToInteger(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Boolean) {
            return (Boolean) value ? 1 : 0;
        } else {
            return null;
        }
    }
}
