package ru.practicum.model.sensor;

import ru.practicum.constant.SensorEventType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MotionSensorEvent extends SensorEvent {
    private Integer linkQuality;
    private Boolean motion;
    private Integer voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
