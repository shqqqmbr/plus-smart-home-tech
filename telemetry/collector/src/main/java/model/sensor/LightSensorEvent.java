package model.sensor;

import constant.SensorEventType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LightSensorEvent extends SensorEvent {
    private Integer linkQuality;
    private Integer luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
