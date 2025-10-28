package event;

import constant.DeviceType;
import constant.SensorEventType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DeviceAddedEvent extends SensorEvent {
    private String id;
    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    @Override
    public SensorEventType getType() {
        return  SensorEventType.DEVICE_ADDED_EVENT;
    }
}
