package ru.practicum.model.hub;

import lombok.Data;
import lombok.ToString;
import ru.practicum.constant.HubEventType;

@Data
@ToString
public class DeviceRemovedEvent extends HubEvent {
    private String id;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED_EVENT;
    }
}
