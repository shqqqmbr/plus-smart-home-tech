package ru.practicum.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.practicum.kafka.KafkaConfig;
import ru.practicum.kafka.KafkaEventProducer;
import ru.practicum.model.sensor.SensorEvent;

@Service
public class SensorService {
    private final KafkaEventProducer producer;
    private final KafkaConfig config;

    public SensorService(KafkaConfig config, KafkaEventProducer producer) {
        this.config = config;
        this.producer = producer;
    }

    public void processSensorEvent(SensorEvent sensorEvent) {
        String topic = config.getTopics().get("sensor-events");
        ProducerRecord<String, SensorEvent> record = new ProducerRecord<>(topic, sensorEvent.getSensorId(), sensorEvent);
        producer.sendRecord(record);
    }
}
