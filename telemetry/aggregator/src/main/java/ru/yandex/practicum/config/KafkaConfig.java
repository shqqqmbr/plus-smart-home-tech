package ru.yandex.practicum.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

@Data
@Component
@ConfigurationProperties("aggregator.kafka")
public class KafkaConfig {
    private Map<String, String> topics;
    private Properties producerProperties;
    private Properties consumerProperties;

    @Bean
    public Map<String, String> getTopics() {
        if (topics == null) {
            topics = Map.of(
                    "sensors-events", "telemetry.sensors.v1",
                    "hubs-events", "telemetry.hubs.v1",
                    "sensors-snapshots", "telemetry.snapshots.v1"
            );
        }
        return topics;
    }

    @Bean
    public Properties getProducerProps() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        props.put("acks", "all");
        props.put("retries", 3);
        props.put("max.in.flight.requests.per.connection", 1);
        props.put("enable.idempotence", "true");
        props.put("linger.ms", 5);
        props.put("batch.size", 16384);
        props.put("buffer.memory", 33554432);
        if (producerProperties != null) {
            props.putAll(producerProperties);
        }
        return props;
    }

    @Bean
    public Properties getConsumerProps() {
        if (consumerProperties == null) {
            consumerProperties = new Properties();
            consumerProperties.put("bootstrap.servers", "localhost:9092");
            consumerProperties.put("group.id", "aggregator-group");
            consumerProperties.put("key.deserializer",
                    "org.apache.kafka.common.serialization.StringDeserializer");
            consumerProperties.put("value.deserializer",
                    "ru.yandex.practicum.SensorEventDeserializer");
            consumerProperties.put("auto.offset.reset", "latest");
            consumerProperties.put("enable.auto.commit", "false");
            consumerProperties.put("isolation.level", "read_committed");
            consumerProperties.put("fetch.min.bytes", 1);
            consumerProperties.put("fetch.max.wait.ms", 500);
            consumerProperties.put("max.poll.records", 500);
            consumerProperties.put("session.timeout.ms", 10000);
            consumerProperties.put("heartbeat.interval.ms", 3000);
        }
        return consumerProperties;
    }
}