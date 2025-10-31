package ru.practicum.model.hub;

import lombok.Data;
import lombok.ToString;
import ru.practicum.constant.ActionType;

@Data
@ToString
public class DeviceAction {
    private String sensorId;
    private ActionType type;
    private Integer value;
}
