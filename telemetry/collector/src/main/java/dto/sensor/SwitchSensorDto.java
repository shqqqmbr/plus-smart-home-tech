package dto.sensor;

import constant.SensorEventType;
import lombok.Data;

@Data
public class SwitchSensorDto extends SensorEventDto {
    private Boolean state;

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
