package ru.practicum.kafka;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Properties;

@Configuration
@ConfigurationProperties("collector.kafka")
@Data
@ToString
public class KafkaConfig {
    private Map<String, String> topics;
    private Properties producerProperties;
}
