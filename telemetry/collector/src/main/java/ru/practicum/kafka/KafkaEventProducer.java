package ru.practicum.kafka;


import lombok.Getter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KafkaEventProducer {
    private final KafkaConfig config;
    private final KafkaProducer<String, SpecificRecordBase> producer;


    public KafkaEventProducer(KafkaConfig config) {
        this.config = config;
        this.producer = new KafkaProducer<>(config.getProducerProperties());
    }

    public void sendRecord(ProducerRecord<String, SpecificRecordBase> record) {
        try (producer) {
            producer.send(record);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
