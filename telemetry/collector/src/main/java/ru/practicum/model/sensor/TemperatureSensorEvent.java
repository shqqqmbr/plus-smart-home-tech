package ru.practicum.model.sensor;

import lombok.Data;
import lombok.ToString;
import ru.practicum.constant.SensorEventType;

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
