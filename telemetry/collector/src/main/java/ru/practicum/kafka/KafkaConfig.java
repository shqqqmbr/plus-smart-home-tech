package ru.practicum.kafka;

import lombok.Data;
import lombok.ToString;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.util.Map;
import java.util.Properties;

@Configuration
@ConfigurationProperties("collector.kafka")
@Data
@ToString
public class KafkaConfig {
    private Map<String, String> topics;
    private Properties producerProperties;

    public Properties getProducerProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.springframework.kafka.support.serializer.JsonSerializer");
        props.put("acks", "all");
        props.put("retries", "3");
        if (producerProperties != null) {
            props.putAll(producerProperties);
        }
        return props;
    }

    @Bean
    public KafkaProducer<String, HubEventAvro> kafkaHubProducer() {
        return new KafkaProducer<>(getProducerProperties());
    }

    @Bean
    public KafkaProducer<String, SensorEventAvro> kafkaSensorProducer() {
        return new KafkaProducer<>(getProducerProperties());
    }
}