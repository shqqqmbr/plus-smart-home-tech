package ru.practicum.model.hub;

import lombok.Data;
import lombok.ToString;
import ru.practicum.constant.ConditionOperation;
import ru.practicum.constant.ConditionType;

@Data
@ToString
public class ScenarioCondition {
    private String sensorId;

    private ConditionType type;

    private ConditionOperation operation;
    private Integer value;
}
