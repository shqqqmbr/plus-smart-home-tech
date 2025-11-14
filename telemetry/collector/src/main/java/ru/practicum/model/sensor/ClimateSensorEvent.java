package ru.practicum.model.sensor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.practicum.constant.SensorEventType;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class ClimateSensorEvent extends SensorEvent {
    private Integer temperatureC;
    private Integer humidity;
    private Integer co2Level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
