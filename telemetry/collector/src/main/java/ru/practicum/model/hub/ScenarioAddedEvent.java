package ru.practicum.model.hub;

import ru.practicum.constant.HubEventType;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ScenarioAddedEvent extends HubEvent {
    @Size(min = 3)
    private String name;
    private ScenarioCondition conditions;
    private DeviceAction actions;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED_EVENT;
    }
}
