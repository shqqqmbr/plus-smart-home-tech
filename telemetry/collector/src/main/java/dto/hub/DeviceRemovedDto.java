package dto.hub;

import constant.HubEventType;
import constant.SensorEventType;
import dto.sensor.SensorEventDto;
import lombok.Data;

@Data
public class DeviceRemovedDto extends HubEventDto {
    private String id;

    @Override
    public HubEventType getType() {
        return  HubEventType.DEVICE_REMOVED_EVENT;
    }
}
