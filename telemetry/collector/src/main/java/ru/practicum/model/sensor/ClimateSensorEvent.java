package ru.practicum.model.sensor;

import lombok.Data;
import lombok.ToString;
import ru.practicum.constant.SensorEventType;

@Data
@ToString
public class ClimateSensorEvent extends SensorEvent {
    private Integer temperatureC;
    private Integer humidity;
    private Integer co2level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR;
    }
}
