package ru.practicum.model.hub;

import ru.practicum.constant.DeviceType;
import ru.practicum.constant.HubEventType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DeviceAddedEvent extends HubEvent {
    private String id;
    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED_EVENT;
    }
}
