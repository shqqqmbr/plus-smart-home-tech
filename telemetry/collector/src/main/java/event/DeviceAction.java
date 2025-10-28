package event;

import constant.ActionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DeviceAction {
    private String sensorId;
    @Enumerated(EnumType.STRING)
    private ActionType type;
    private Integer value;
}
