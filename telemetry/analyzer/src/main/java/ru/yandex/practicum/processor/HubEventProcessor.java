package ru.yandex.practicum.processor;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.mapper.ScenarioMapper;
import ru.yandex.practicum.mapper.SensorMapper;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.model.Sensor;
import ru.yandex.practicum.service.ScenarioService;
import ru.yandex.practicum.service.SensorService;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {

    private final KafkaConsumer<String, HubEventAvro> hubConsumer;
    private final KafkaConfig kafkaConfig;

    private final SensorService sensorService;
    private final ScenarioService scenarioService;


    public HubEventProcessor(KafkaConfig kafkaConfig,
                             SensorService sensorService,
                             ScenarioService scenarioService) {
        this.kafkaConfig = kafkaConfig;
        hubConsumer = new KafkaConsumer<>(kafkaConfig.getHubConsumerProperties());
        this.sensorService = sensorService;
        this.scenarioService = scenarioService;

    }


    @Override
    public void run() {
        log.info("HubEventProcessor started");

        try (hubConsumer) {
            Runtime.getRuntime().addShutdownHook(new Thread(hubConsumer::wakeup));
            hubConsumer.subscribe(List.of(kafkaConfig.getTopics().get("hubs-events")));

            while (true) {
                ConsumerRecords<String, HubEventAvro> records = hubConsumer.poll(Duration.ofSeconds(5));
                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    HubEventAvro hubEventAvro = record.value();
                    switch (hubEventAvro.getPayload()) {

                        case DeviceAddedEventAvro deviceAddedEventAvro -> {
                            Sensor addedSensor = SensorMapper.avroToSensor(hubEventAvro.getHubId(), deviceAddedEventAvro);
                            sensorService.add(addedSensor);
                        }

                        case DeviceRemovedEventAvro deviceRemovedEventAvro ->
                                sensorService.delete(hubEventAvro.getHubId(), deviceRemovedEventAvro.getId());


                        case ScenarioAddedEventAvro scenarioAddedEventAvro -> {
                            Scenario addedScenario = ScenarioMapper.avroToScenario(hubEventAvro.getHubId(), scenarioAddedEventAvro);
                            scenarioService.upsert(addedScenario);
                        }

                        case ScenarioRemovedEventAvro scenarioRemovedEventAvro ->
                                scenarioService.delete(scenarioRemovedEventAvro.getName(), hubEventAvro.getHubId());

                        default -> throw new IllegalStateException("Unexpected value: " + hubEventAvro.getPayload());
                    }
                }

                hubConsumer.commitAsync((offsets, exception) -> {
                    if (exception != null) {
                        log.warn("Commit hubEvent processing error. Offsets: {}", offsets, exception);
                    }
                });
            }
        } catch (WakeupException ignored) {

        } catch (Exception e) {
            log.error("Analyzer. Error by handling HubEvents from kafka", e);
        } finally {
            log.info("Analyzer. Closing consumer.");
        }
    }

}
