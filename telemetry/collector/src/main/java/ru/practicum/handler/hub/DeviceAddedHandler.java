package ru.practicum.handler.hub;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.practicum.constant.HubEventType;
import ru.practicum.handler.mapper.EnumMapper;
import ru.practicum.model.hub.DeviceAddedEvent;
import ru.practicum.model.hub.HubEvent;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component(value = "DEVICE_ADDED")
@AllArgsConstructor
public class DeviceAddedHandler implements HubEventHandler {
    private final KafkaProducer<String, HubEventAvro> kafkaProducer;

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_ADDED;
    }

    @Override
    public void handle(HubEvent event) {
        DeviceAddedEvent ev = (DeviceAddedEvent) event;
        DeviceAddedEventAvro deviceAddedEventAvro = DeviceAddedEventAvro.newBuilder()
                .setId(ev.getId())
                .setType(EnumMapper.map(ev.getDeviceType(), DeviceTypeAvro.class))
                .build();
        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(ev.getHubId())
                .setTimestamp(ev.getTimestamp())
                .setPayload(deviceAddedEventAvro)
                .build();
        ProducerRecord<String, HubEventAvro> record = new ProducerRecord<>(
                "telemetry.hubs.v1",
                hubEventAvro
        );
        kafkaProducer.send(record);
    }
}
