package ru.practicum.model.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import ru.practicum.constant.HubEventType;

import java.util.List;

@Data
@ToString
public class ScenarioAddedEvent extends HubEvent {
    @Size(min = 3)
    @NotBlank
    private String name;
    @NotNull
    private List<ScenarioCondition> conditions;
    @NotNull
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
