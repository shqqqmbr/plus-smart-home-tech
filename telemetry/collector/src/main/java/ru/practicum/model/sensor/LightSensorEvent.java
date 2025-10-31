package ru.practicum.model.sensor;

import lombok.Data;
import lombok.ToString;
import ru.practicum.constant.SensorEventType;

@Data
@ToString
public class LightSensorEvent extends SensorEvent {
    private Integer linkQuality;
    private Integer luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR;
    }
}
