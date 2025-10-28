package event;

import constant.SensorEventType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DeviceRemovedEvent extends SensorEvent {
    private String id;

    @Override
    public SensorEventType getType() {
        return SensorEventType.DEVICE_REMOVED_EVENT;
    }
}
