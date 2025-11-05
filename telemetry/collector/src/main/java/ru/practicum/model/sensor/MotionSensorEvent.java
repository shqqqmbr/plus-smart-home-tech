package ru.practicum.model.sensor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.practicum.constant.SensorEventType;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class MotionSensorEvent extends SensorEvent {
    private Integer linkQuality;
    private Boolean motion;
    private Integer voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR;
    }
}
