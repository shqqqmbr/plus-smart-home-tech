package ru.practicum.model.hub;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import ru.practicum.constant.HubEventType;

@Data
@ToString
public class ScenarioRemovedEvent extends HubEvent {
    @Size(min = 3)
    private String name;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED_EVENT;
    }
}
