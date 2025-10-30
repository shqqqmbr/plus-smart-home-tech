package dto.hub;

import constant.HubEventType;
import constant.SensorEventType;
import dto.sensor.SensorEventDto;
import model.hub.DeviceAction;
import model.hub.ScenarioCondition;
import lombok.Data;

@Data
public class ScenarioAddedDto extends HubEventDto {
    private String name;
    private ScenarioCondition conditions;
    private DeviceAction actions;

    @Override
    public HubEventType getType() {
        return  HubEventType.SCENARIO_ADDED_EVENT;
    }
}
