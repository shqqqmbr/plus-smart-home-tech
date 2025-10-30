package ru.practicum.model.hub;

import lombok.Data;
import lombok.ToString;
import ru.practicum.constant.DeviceType;
import ru.practicum.constant.HubEventType;

@Data
@ToString
public class DeviceAddedEvent extends HubEvent {
    private String id;
    private DeviceType deviceType;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED_EVENT;
    }
}
