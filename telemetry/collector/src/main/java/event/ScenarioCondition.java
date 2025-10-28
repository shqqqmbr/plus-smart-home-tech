package event;

import constant.ConditionType;
import constant.Operation;
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
    private Operation operation;
    private Integer value;
}
