package ru.yandex.practicum.processor;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Scenario;

import java.util.Set;

@Slf4j
@Service
public class HubRouterProcessor {

    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public HubRouterProcessor(@Value("${grpc.client.hub-router.address}") String address) {
        ManagedChannel channel = ManagedChannelBuilder.forTarget(address)
                .usePlaintext()
                .build();
        this.hubRouterClient = HubRouterControllerGrpc.newBlockingStub(channel);
        log.info("HubRouterProcessor initialized with address: {}", address);
    }

    public void executeAction(Action action, String hubId) {
        try {
            DeviceActionRequest request = DeviceActionRequest.newBuilder()
                    .setHubId(hubId)
                    .setScenarioName(action.getScenario().getName())
                    .setAction(DeviceActionProto.newBuilder()
                            .setSensorId(action.getSensorId())
                            .setType(ActionTypeProto.valueOf(action.getType().name()))
                            .setValue(action.getValue() != null ? action.getValue() : 0)
                            .build())
                    .build();

            log.info("Sending action to hub {}: scenario={}, device={}, type={}, value={}",
                    hubId, action.getScenario().getName(), action.getSensorId(),
                    action.getType(), action.getValue());

            hubRouterClient.handleDeviceAction(request);

            log.info("Action sent successfully to hub {}", hubId);

        } catch (Exception e) {
            log.error("Failed to send action to hub {}: {}", hubId, e.getMessage(), e);
            throw new RuntimeException("Failed to execute action", e);
        }
    }

    public void sendScenarioActions(Set<Action> actions, String hubId) {
        if (actions == null || actions.isEmpty()) {
            log.warn("No actions to send for hub: {}", hubId);
            return;
        }

        log.info("Sending {} actions for hub {}", actions.size(), hubId);

        for (Action action : actions) {
            try {
                log.debug("Executing action for hub {} scenario {} sensor {} type {} value {}",
                        hubId,
                        action.getScenario() != null ? action.getScenario().getName() : "null",
                        action.getSensorId(),
                        action.getType(),
                        action.getValue());
                executeAction(action, hubId);
            } catch (Exception e) {
                log.error("Failed to send action {} for hub {}: {}",
                        action.getSensorId(), hubId, e.getMessage());
            }
        }

        log.info("All actions sent for hub {}", hubId);
    }

    public void executeScenarioActions(Scenario scenario, String hubId) {
        if (scenario == null || scenario.getScenarioActions() == null || scenario.getScenarioActions().isEmpty()) {
            log.warn("No actions in scenario {} for hub {}",
                    scenario != null ? scenario.getName() : "null", hubId);
            return;
        }

        log.info("Executing actions for scenario '{}' on hub {}", scenario.getName(), hubId);
        sendScenarioActions(scenario.getScenarioActions(), hubId);
    }
}