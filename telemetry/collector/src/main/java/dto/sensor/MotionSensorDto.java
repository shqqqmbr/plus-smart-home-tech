package dto.sensor;

import constant.SensorEventType;
import lombok.Data;

@Data
public class MotionSensorDto extends SensorEventDto {
    private Integer linkQuality;
    private Boolean motion;
    private Integer voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
