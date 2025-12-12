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
        if (producerProperties == null) {
            producerProperties = new Properties();
            producerProperties.put("bootstrap.servers", "localhost:9092");
            producerProperties.put("acks", "all");
            producerProperties.put("retries", 3);
            producerProperties.put("max.in.flight.requests.per.connection", 1);
            producerProperties.put("enable.idempotence", "true");
            producerProperties.put("linger.ms", 5);
            producerProperties.put("batch.size", 16384);
            producerProperties.put("buffer.memory", 33554432);
        }
        return producerProperties;
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