package dto.hub;

import constant.DeviceType;
import constant.HubEventType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class DeviceAddedDto extends HubEventDto {
    private String id;
    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED_EVENT;
    }
}
