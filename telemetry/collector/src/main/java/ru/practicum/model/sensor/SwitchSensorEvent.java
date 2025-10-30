package ru.practicum.model.sensor;

import ru.practicum.constant.SensorEventType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SwitchSensorEvent extends SensorEvent {
    private Boolean state;

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
