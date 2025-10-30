package ru.practicum.model.hub;

import ru.practicum.constant.HubEventType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DeviceRemovedEvent extends HubEvent {
    private String id;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED_EVENT;
    }
}
