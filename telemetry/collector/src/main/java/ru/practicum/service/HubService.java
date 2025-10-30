package ru.practicum.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.practicum.kafka.KafkaConfig;
import ru.practicum.kafka.KafkaEventProducer;
import ru.practicum.model.hub.HubEvent;

@Service
public class HubService {
    private final KafkaEventProducer producer;
    private final KafkaConfig config;

    public HubService(KafkaEventProducer producer, KafkaConfig config) {
        this.producer = producer;
        this.config = config;
    }

    public void processHubEvent(HubEvent hubEvent) {
        String topic = config.getTopics().get("hub-events");
        ProducerRecord<String, HubEvent> record = new ProducerRecord<>(topic, hubEvent.getHubId(), hubEvent);
        producer.sendRecord(record);
    }
}
