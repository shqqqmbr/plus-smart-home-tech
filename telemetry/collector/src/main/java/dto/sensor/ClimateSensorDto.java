package dto.sensor;

import constant.SensorEventType;
import lombok.Data;

@Data
public class ClimateSensorDto extends SensorEventDto {
    private Integer temperatureC;
    private Integer humidity;
    private Integer co2level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
