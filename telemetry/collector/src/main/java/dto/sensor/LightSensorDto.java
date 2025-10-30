package dto.sensor;

import constant.SensorEventType;
import lombok.Data;

@Data
public class LightSensorDto extends SensorEventDto {
    private Integer linkQuality;
    private Integer luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
