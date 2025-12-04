package ru.practicum.kafka;

import lombok.Data;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.util.Map;
import java.util.Properties;

@Data
@Configuration
@ConfigurationProperties("collector.kafka")
public class KafkaConfig {
    private Map<String, String> topics;
    private Properties producerProperties;

    public Properties getProducerProperties() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "ru.practicum.kafka.AvroSerializer");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        if (producerProperties != null) {
            props.putAll(producerProperties);
        }
        return props;
    }

    @Bean
    public String getHubTopic() {
        return topics != null ? topics.get("telemetry-hubs") : "telemetry.hubs.v1";
    }

    @Bean
    public String getSensorTopic() {
        return topics != null ? topics.get("telemetry-sensors") : "telemetry.sensors.v1";
    }

    @Bean
    public String hubTopic() {
        return getHubTopic();
    }

    @Bean
    public String sensorTopic() {
        return getSensorTopic();
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