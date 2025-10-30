package model.hub;

import constant.SensorEventType;
import model.sensor.SensorEvent;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ScenarioAddedEvent extends SensorEvent {
    @Size(min = 3)
    private String name;
    private ScenarioCondition conditions;
    private DeviceAction actions;
    @Override
    public SensorEventType getType() {
        return SensorEventType.SCENARIO_ADDED_EVENT;
    }
}
