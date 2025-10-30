package dto.sensor;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import constant.SensorEventType;
import dto.hub.DeviceAddedDto;
import dto.hub.DeviceRemovedDto;
import dto.hub.ScenarioAddedDto;
import dto.hub.ScenarioRemovedDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClimateSensorDto.class, name = "CLIMATE_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = LightSensorDto.class, name = "LIGHT_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = MotionSensorDto.class, name = "MOTION_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = SwitchSensorDto.class, name = "SWITCH_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = TemperatureSensorDto.class, name = "TEMPERATURE_SENSOR_EVENT"),
})

@Data
public abstract class SensorEventDto {
    @NotBlank
    private String id;
    @NotBlank
    private String hubId;
    private String timestamp;

    @NotNull
    public abstract SensorEventType getType();
}
