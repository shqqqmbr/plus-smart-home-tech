package ru.practicum.handler.hub;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.practicum.constant.HubEventType;
import ru.practicum.handler.mapper.EnumMapper;
import ru.practicum.kafka.KafkaConfig;
import ru.yandex.practicum.grpc.telemetry.collector.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.collector.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@Component(value = "DEVICE_ADDED")
@AllArgsConstructor
public class DeviceAddedHandler implements HubEventHandler {
    private final KafkaProducer<String, HubEventAvro> kafkaProducer;
    private final KafkaConfig kafkaConfig;

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_ADDED_EVENT;
    }

    @Override
    public void handle(HubEventProto event) {
        DeviceAddedEventProto ev = event.getDeviceAdded();
        DeviceAddedEventAvro deviceAddedEventAvro = DeviceAddedEventAvro.newBuilder()
                .setId(ev.getId())
                .setType(EnumMapper.map(ev.getType(), DeviceTypeAvro.class))
                .build();
        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getEpochSecond()))
                .setPayload(deviceAddedEventAvro)
                .build();
        ProducerRecord<String, HubEventAvro> record = new ProducerRecord<>(
//                или же нужно инжектить через аннотацию value?
                kafkaConfig.getHubTopic(),
                hubEventAvro
        );
        kafkaProducer.send(record);
    }
}