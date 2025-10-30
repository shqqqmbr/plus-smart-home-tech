package model.hub;

import constant.ConditionType;
import constant.ConditionOperation;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ScenarioCondition {
    private String sensorId;
    @Enumerated(EnumType.STRING)
    private ConditionType type;
    @Enumerated(EnumType.STRING)
    private ConditionOperation operation;
    private Integer value;
}
