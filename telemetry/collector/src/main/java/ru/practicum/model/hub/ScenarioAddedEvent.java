package ru.practicum.model.hub;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import ru.practicum.constant.HubEventType;

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
