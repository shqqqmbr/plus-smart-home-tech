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
    private Properties producerProps;
    private Properties consumerProps;

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
        if (producerProps == null) {
            producerProps = new Properties();
            producerProps.put("bootstrap.servers", "localhost:9092");
            producerProps.put("key.serializer",
                    "org.apache.kafka.common.serialization.StringSerializer");

            // ВАЖНО: Используем правильный Avro сериализатор
            producerProps.put("value.serializer",
                    "ru.yandex.practicum.GeneralAvroSerializer");

            // ОБЯЗАТЕЛЬНО: URL Schema Registry
            producerProps.put("schema.registry.url", "http://localhost:8081");

            // Опционально: использовать конкретные Avro классы
            producerProps.put("specific.avro.reader", "true");

            // Настройки производительности
            producerProps.put("acks", "all");
            producerProps.put("retries", 3);
            producerProps.put("max.in.flight.requests.per.connection", 1);
            producerProps.put("enable.idempotence", "true");
            producerProps.put("linger.ms", 5);
            producerProps.put("batch.size", 16384);
            producerProps.put("buffer.memory", 33554432);

            // Убираем JSON-настройки
            // producerProps.put("spring.json.add.type.headers", "false");
        }
        return producerProps;
    }

    @Bean
    public Properties getConsumerProps() {
        if (consumerProps == null) {
            consumerProps = new Properties();
            consumerProps.put("bootstrap.servers", "localhost:9092");
            consumerProps.put("group.id", "aggregator-group");
            consumerProps.put("key.deserializer",
                    "org.apache.kafka.common.serialization.StringDeserializer");

            // ВАРИАНТ 1: Использовать KafkaAvroDeserializer (рекомендуется)
            // consumerProps.put("value.deserializer",
            //     "io.confluent.kafka.serializers.KafkaAvroDeserializer");
            // consumerProps.put("schema.registry.url", "http://localhost:8081");
            // consumerProps.put("specific.avro.reader", "true");

            // ВАРИАНТ 2: Использовать ByteArrayDeserializer + ручную десериализацию
            // (как у вас сейчас в AggregationStarter)
            consumerProps.put("value.deserializer",
                    "ru.yandex.practicum.SensorEventDeserializer");
            // Schema registry URL не нужен для ByteArrayDeserializer

            // Настройки consumer
            consumerProps.put("auto.offset.reset", "latest");
            consumerProps.put("enable.auto.commit", "false");
            consumerProps.put("isolation.level", "read_committed");
            consumerProps.put("fetch.min.bytes", 1);
            consumerProps.put("fetch.max.wait.ms", 500);
            consumerProps.put("max.poll.records", 500);
            consumerProps.put("session.timeout.ms", 10000);
            consumerProps.put("heartbeat.interval.ms", 3000);
        }
        return consumerProps;
    }
}