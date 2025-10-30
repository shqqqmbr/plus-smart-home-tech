package dto.sensor;

import constant.SensorEventType;
import lombok.Data;

@Data
public class TemperatureSensorDto extends SensorEventDto {
    private Integer temperatureC;
    private Integer temperatureF;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
