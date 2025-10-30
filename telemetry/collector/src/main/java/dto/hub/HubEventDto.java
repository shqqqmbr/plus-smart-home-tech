package dto.hub;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import constant.HubEventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceAddedDto.class, name = "DEVICE_ADDED_EVENT"),
        @JsonSubTypes.Type(value = DeviceRemovedDto.class, name = "DEVICE_REMOVED_EVENT"),
        @JsonSubTypes.Type(value = ScenarioAddedDto.class, name = "SCENARIO_ADDED_EVENT"),
        @JsonSubTypes.Type(value = ScenarioRemovedDto.class, name = "SCENARIO_REMOVED_EVENT"),
})
@Data
public abstract class HubEventDto {
    @NotBlank
    private String hibId;
    private String timestamp;

    @NotNull
    public abstract HubEventType getType();
}
