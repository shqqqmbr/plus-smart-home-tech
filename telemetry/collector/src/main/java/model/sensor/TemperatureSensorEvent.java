package model.sensor;

import constant.SensorEventType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TemperatureSensorEvent extends SensorEvent {
    private Integer temperatureC;
    private Integer temperatureF;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
