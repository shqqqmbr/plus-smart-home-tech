package ru.practicum.model.hub;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.practicum.constant.HubEventType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

import java.time.Instant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        defaultImpl = HubEventType.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceAddedEvent.class, name = "DEVICE_ADDED_EVENT"),
        @JsonSubTypes.Type(value = DeviceRemovedEvent.class, name = "DEVICE_REMOVED_EVENT"),
        @JsonSubTypes.Type(value = ScenarioAddedEvent.class, name = "SCENARIO_ADDED_EVENT"),
        @JsonSubTypes.Type(value = ScenarioRemovedEvent.class, name = "SCENARIO_REMOVED_EVENT"),
})
@Data
@ToString
public abstract class HubEvent {
    @NotBlank
    private String hubId;
    private final Instant timestamp = Instant.now();

    @NotBlank
    public abstract HubEventType getType();
}
