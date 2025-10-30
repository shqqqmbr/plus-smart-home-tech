package ru.practicum.kafka;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Getter
public class KafkaEventProducer<T> {
    private final KafkaConfig config;
    private final KafkaProducer<String, T> producer;


    public KafkaEventProducer(KafkaConfig config) {
        this.config = config;
        this.producer = new KafkaProducer<>(config.getProducerProperties());
    }

    public void sendRecord(ProducerRecord<String, T> record) {
        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                log.error("Failed to send message to topic: {}", record.topic(), exception);
            } else {
                log.info("Message sent successfully to topic: {}, partition: {}, offset: {}",
                        metadata.topic(), metadata.partition(), metadata.offset());
            }
        });
    }
}
