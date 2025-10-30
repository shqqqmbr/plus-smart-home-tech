package dto.hub;

import constant.HubEventType;
import lombok.Data;

@Data
public class ScenarioRemovedDto extends HubEventDto {
    private String name;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED_EVENT;
    }
}
