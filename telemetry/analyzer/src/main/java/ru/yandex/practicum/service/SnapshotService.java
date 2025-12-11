package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.ConditionOperation;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.model.ScenarioCondition;
import ru.yandex.practicum.processor.HubRouterProcessor;
import ru.yandex.practicum.repository.ScenarioRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SnapshotService {

    private final ScenarioRepository scenarioRepository;
    private final HubRouterProcessor hubRouterProcessor;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SnapshotService.class);

    public void analyze(SensorsSnapshotAvro sensorsSnapshot) {
        String hubId = sensorsSnapshot.getHubId();
        Set<Scenario> hubScenario = scenarioRepository.findByHubId(hubId);

        for (Scenario scenario : hubScenario) {
            boolean triggered = isScenarioTriggered(scenario, sensorsSnapshot);
            log.debug("Scenario '{}' for hub {} triggered={}", scenario.getName(), hubId, triggered);
            if (triggered) {
                if (scenario.getScenarioActions() == null) {
                    log.warn("Scenario '{}' for hub {} has no actions loaded", scenario.getName(), hubId);
                } else {
                    log.info("Scenario '{}' for hub {} matched. Sending {} actions", scenario.getName(), hubId, scenario.getScenarioActions().size());
                }
                hubRouterProcessor.sendScenarioActions(scenario.getScenarioActions(), hubId);
            } else {
                log.debug("Scenario '{}' for hub {} not triggered", scenario.getName(), hubId);
            }
        }
    }

    private boolean isScenarioTriggered(Scenario scenario, SensorsSnapshotAvro sensorsSnapshot) {
        if (scenario.getScenarioConditions() == null || scenario.getScenarioConditions().isEmpty()) {
            log.warn("Scenario '{}' for hub {} has no conditions", scenario.getName(), scenario.getHubId());
            return false;
        }
        boolean all = true;
        for (ScenarioCondition condition : scenario.getScenarioConditions()) {
            boolean ok = isConditionTriggered(condition, sensorsSnapshot);
            log.debug("Condition check for scenario '{}' hub {} sensor {} type {} op {} value {} -> {}",
                    scenario.getName(), scenario.getHubId(), condition.getSensorId(), condition.getType(),
                    condition.getOperation(), condition.getValue(), ok);
            if (!ok) {
                all = false;
                break;
            }
        }
        return all;
    }

    private boolean isConditionTriggered(ScenarioCondition condition, SensorsSnapshotAvro snapshot) {
        SensorsStateAvro sensorState = snapshot.getSensorsState().get(condition.getSensorId());

        if (sensorState == null) {
            log.debug("No state for sensor {} in snapshot of hub {}", condition.getSensorId(), snapshot.getHubId());
            return false;
        }

        if (sensorState.getData() == null) {
            log.debug("No data in state for sensor {} (hub {})", condition.getSensorId(), snapshot.getHubId());
            return false;
        }

        try {
            switch (condition.getType()) {
                case SWITCH -> {
                    if (!(sensorState.getData() instanceof SwitchSensorAvro)) {
                        return false;
                    }
                    SwitchSensorAvro switchSensor = (SwitchSensorAvro) sensorState.getData();
                    int switchStatus = switchSensor.getState() ? 1 : 0;
                    return isCompare(switchStatus, condition.getOperation(), condition.getValue());
                }

                case LUMINOSITY -> {
                    if (!(sensorState.getData() instanceof LightSensorAvro)) {
                        return false;
                    }
                    LightSensorAvro lightSensor = (LightSensorAvro) sensorState.getData();
                    return isCompare(lightSensor.getLuminosity(), condition.getOperation(), condition.getValue());
                }

                case CO2LEVEL, TEMPERATURE, HUMIDITY -> {
                    if (!(sensorState.getData() instanceof ClimateSensorAvro)) {
                        return false;
                    }
                    ClimateSensorAvro climateSensor = (ClimateSensorAvro) sensorState.getData();
                    int value = switch (condition.getType()) {
                        case CO2LEVEL -> climateSensor.getCo2Level();
                        case TEMPERATURE -> climateSensor.getTemperatureC();
                        case HUMIDITY -> climateSensor.getHumidity();
                        default -> throw new IllegalStateException("Unexpected climate type");
                    };
                    return isCompare(value, condition.getOperation(), condition.getValue());
                }

                case MOTION -> {
                    if (!(sensorState.getData() instanceof MotionSensorAvro)) {
                        return false;
                    }
                    MotionSensorAvro motionSensor = (MotionSensorAvro) sensorState.getData();
                    int motionValue = motionSensor.getMotion() ? 1 : 0;
                    return isCompare(motionValue, condition.getOperation(), condition.getValue());
                }

                default -> {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isCompare(int sensorValue, ConditionOperation operation, int conditionValue) {
        boolean result = switch (operation) {
            case EQUALS -> sensorValue == conditionValue;
            case GREATER_THAN -> sensorValue > conditionValue;
            case LOWER_THAN -> sensorValue < conditionValue;
        };
        return result;
    }
}