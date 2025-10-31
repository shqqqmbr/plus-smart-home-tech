package ru.practicum.model.sensor;

import lombok.Data;
import lombok.ToString;
import ru.practicum.constant.SensorEventType;

@Data
@ToString
public class SwitchSensorEvent extends SensorEvent {
    private Boolean state;

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR;
    }
}
