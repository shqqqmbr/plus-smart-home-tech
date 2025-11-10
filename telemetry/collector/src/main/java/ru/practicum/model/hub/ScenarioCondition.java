package ru.practicum.model.hub;

import lombok.Data;
import lombok.ToString;
import ru.practicum.constant.ConditionType;
import ru.practicum.constant.OperationType;

@Data
@ToString
public class ScenarioCondition {
    private String sensorId;

    private ConditionType type;

    private OperationType operation;
    private Integer value;
}
