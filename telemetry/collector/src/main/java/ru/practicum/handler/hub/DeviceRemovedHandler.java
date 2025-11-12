package ru.practicum.handler.hub;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.practicum.constant.HubEventType;
import ru.practicum.kafka.KafkaConfig;
import ru.practicum.model.hub.DeviceRemovedEvent;
import ru.practicum.model.hub.HubEvent;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component(value = "DEVICE_REMOVED")
@AllArgsConstructor
public class DeviceRemovedHandler implements HubEventHandler {
    private final KafkaProducer<String, HubEventAvro> kafkaProducer;
    private final KafkaConfig kafkaConfig;

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_REMOVED_EVENT;
    }

    @Override
    public void handle(HubEvent event) {
        DeviceRemovedEvent ev = (DeviceRemovedEvent) event;
        DeviceRemovedEventAvro deviceRemovedEvent = DeviceRemovedEventAvro.newBuilder()
                .setId(ev.getId())
                .build();
        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(ev.getHubId())
                .setTimestamp(ev.getTimestamp())
                .setPayload(deviceRemovedEvent)
                .build();
        ProducerRecord<String, HubEventAvro> record = new ProducerRecord<>(
                kafkaConfig.getHubTopic(),
                hubEventAvro
        );
        kafkaProducer.send(record);
    }
}
