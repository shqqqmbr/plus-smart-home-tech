package model.hub;

import constant.SensorEventType;
import model.sensor.SensorEvent;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ScenarioRemovedEvent extends SensorEvent {
    @Size(min = 3)
    private String name;

    @Override
    public SensorEventType getType() {
        return SensorEventType.DEVICE_REMOVED_EVENT;
    }
}
