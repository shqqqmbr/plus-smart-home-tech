package ru.yandex.practicum.starter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsStateAvro;
import ru.yandex.practicum.repository.SnapshotsRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Класс AggregationStarter, ответственный за запуск агрегации данных.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {

    private final KafkaConfig kafkaConfig;
    private final SnapshotsRepository snapshotsRepository;

    private KafkaProducer<String, SensorsSnapshotAvro> producer;
    private KafkaConsumer<String, byte[]> consumer;
    private final SpecificDatumReader<SensorEventAvro> avroReader = new SpecificDatumReader<>(SensorEventAvro.class);

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Thread mainThread;

    /**
     * Метод для начала процесса агрегации данных.
     * Подписывается на топики для получения событий от датчиков,
     * формирует снимок их состояния и записывает в кафку.
     */
    public void start() {
        if (running.compareAndSet(false, true)) {
            log.info("=== AGGREGATOR STARTING ===");

            executorService.submit(() -> {
                mainThread = Thread.currentThread();
                runAggregationProcess();
            });

            log.info("Aggregator started successfully");
        } else {
            log.warn("Aggregator is already running");
        }
    }

    private void runAggregationProcess() {
        try {
            // Инициализация продюсера и консьюмера
            initializeProducerAndConsumer();

            String sensorsTopic = kafkaConfig.getTopics().get("sensors-events");
            String snapshotsTopic = kafkaConfig.getTopics().get("sensors-snapshots");

            if (sensorsTopic == null || snapshotsTopic == null) {
                log.error("Topics not configured correctly!");
                running.set(false);
                return;
            }

            log.info("Subscribing to sensors topic: {}", sensorsTopic);
            log.info("Will publish snapshots to topic: {}", snapshotsTopic);

            // Проверка соответствия топиков тестовым
            if (!sensorsTopic.equals("telemetry.sensors.v1")) {
                log.warn("Warning: Aggregator is subscribed to '{}' but test expects 'telemetry.sensors.v1'", sensorsTopic);
            }
            if (!snapshotsTopic.equals("telemetry.snapshots.v1")) {
                log.warn("Warning: Aggregator will publish to '{}' but test expects 'telemetry.snapshots.v1'", snapshotsTopic);
            }

            // Подписка на топик
            consumer.subscribe(List.of(sensorsTopic));
            log.info("Successfully subscribed to {}", sensorsTopic);

            // Цикл обработки событий
            while (running.get()) {
                try {
                    ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(1000));

                    if (!records.isEmpty()) {
                        log.debug("Received {} raw messages", records.count());
                        processRecords(records, snapshotsTopic);
                    }

                } catch (WakeupException e) {
                    if (running.get()) {
                        log.info("Consumer woke up, continuing...");
                    }
                } catch (Exception e) {
                    log.error("Error during polling: {}", e.getMessage(), e);
                    if (running.get()) {
                        Thread.sleep(1000); // Пауза при ошибке
                    }
                }
            }

        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер и продюсер в блоке finally
            log.info("WakeupException caught, shutting down gracefully");
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            shutdownGracefully();
        }
    }

    private void initializeProducerAndConsumer() {
        this.producer = new KafkaProducer<>(kafkaConfig.getProducerProps());

        Properties consumerProps = new Properties();
        consumerProps.putAll(kafkaConfig.getConsumerProps());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        consumerProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
        consumerProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);

        this.consumer = new KafkaConsumer<>(consumerProps);
    }

    private void processRecords(ConsumerRecords<String, byte[]> records, String snapshotsTopic) {
        Map<String, Optional<SensorsSnapshotAvro>> snapshotsToSend = new HashMap<>();
        Set<String> processedHubs = new HashSet<>();

        for (ConsumerRecord<String, byte[]> record : records) {
            try {
                SensorEventAvro event = deserializeAvro(record.value());

                log.debug("Processing event: hub={}, sensor={}, type={}, offset={}",
                        event.getHubId(), event.getId(),
                        event.getPayload().getClass().getSimpleName(), record.offset());

                Optional<SensorsSnapshotAvro> updatedSnapshot = updateState(event);
                if (updatedSnapshot.isPresent()) {
                    snapshotsToSend.put(event.getHubId(), updatedSnapshot);
                    processedHubs.add(event.getHubId());
                }

            } catch (IOException e) {
                log.error("Failed to deserialize Avro message at offset {}: {}",
                        record.offset(), e.getMessage());
            } catch (Exception e) {
                log.error("Error processing event at offset {}: {}",
                        record.offset(), e.getMessage(), e);
            }
        }

        // Отправка всех снапшотов
        for (Map.Entry<String, Optional<SensorsSnapshotAvro>> entry : snapshotsToSend.entrySet()) {
            String hubId = entry.getKey();
            Optional<SensorsSnapshotAvro> snapshotOpt = entry.getValue();

            snapshotOpt.ifPresent(snapshot -> {
                sendSnapshot(snapshot, snapshotsTopic);
                log.info("Snapshot prepared for hub: {} with {} sensors",
                        hubId, snapshot.getSensorsState().size());
            });
        }

        // Фиксация оффсетов
        try {
            consumer.commitSync();
            if (!processedHubs.isEmpty()) {
                log.debug("Committed offsets for {} hubs", processedHubs.size());
            }
        } catch (Exception e) {
            log.error("Failed to commit offsets: {}", e.getMessage());
        }
    }

    private SensorEventAvro deserializeAvro(byte[] avroBytes) throws IOException {
        BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(avroBytes, null);
        return avroReader.read(null, decoder);
    }

    private boolean hasDataChanged(Object oldData, Object newData) {
        if (oldData == null || newData == null) return true;
        if (!oldData.getClass().equals(newData.getClass())) return true;

        // Сравниваем сериализованные байты для точности
        try {
            byte[] bytes1 = serializeAvro(oldData);
            byte[] bytes2 = serializeAvro(newData);
            return !Arrays.equals(bytes1, bytes2);
        } catch (IOException e) {
            log.warn("Failed to serialize for comparison: {}", e.getMessage());
            // Fallback: сравниваем через toString
            return !oldData.toString().equals(newData.toString());
        }
    }

    private byte[] serializeAvro(Object avro) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);

        // Получаем схему из объекта Avro
        org.apache.avro.Schema schema = org.apache.avro.specific.SpecificData.get().getSchema(avro.getClass());

        // Создаем writer с правильной схемой
        org.apache.avro.io.DatumWriter<Object> writer =
                new org.apache.avro.specific.SpecificDatumWriter<>(schema);

        writer.write(avro, encoder);
        encoder.flush();
        return out.toByteArray();
    }

    private Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        Optional<SensorsSnapshotAvro> oldSnapshotOpt = snapshotsRepository.get(event.getHubId());

        // Первый снапшот для хаба
        if (!oldSnapshotOpt.isPresent()) {
            log.info("Creating first snapshot for hub: {}", event.getHubId());
            return createAndSaveSnapshot(event, Collections.emptyMap());
        }

        SensorsSnapshotAvro oldSnapshot = oldSnapshotOpt.get();
        Map<String, SensorsStateAvro> oldStates = oldSnapshot.getSensorsState();
        SensorsStateAvro oldState = oldStates.get(event.getId());

        // Новый сенсор для хаба
        if (oldState == null) {
            log.info("New sensor {} for hub: {}", event.getId(), event.getHubId());
            return createAndSaveSnapshot(event, oldStates);
        }

        // Проверка таймстампа
//        if (!oldState.getTimestamp().isBefore(event.getTimestamp())) {
//            log.debug("Event timestamp is not newer for sensor: {}", event.getId());
//            return Optional.empty();
//        }

        // Проверка изменения данных
        Object oldPayload = oldState.getData();
        Object newPayload = event.getPayload();

        if (hasDataChanged(oldPayload, newPayload)) {
            log.info("Data changed for sensor: {} in hub: {}", event.getId(), event.getHubId());
            return createAndSaveSnapshot(event, oldStates);
        } else {
            log.debug("Data unchanged for sensor: {}", event.getId());
            return Optional.empty();
        }
    }

    private Optional<SensorsSnapshotAvro> createAndSaveSnapshot(SensorEventAvro event,
                                                                Map<String, SensorsStateAvro> existingStates) {
        Map<String, SensorsStateAvro> newStates = new HashMap<>(existingStates);

        SensorsStateAvro sensorState = new SensorsStateAvro();
        sensorState.setTimestamp(event.getTimestamp());
        sensorState.setData(event.getPayload());

        newStates.put(event.getId(), sensorState);

        SensorsSnapshotAvro newSnapshot = SensorsSnapshotAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.now())
                .setSensorsState(newStates)
                .build();

        snapshotsRepository.update(event.getHubId(), newSnapshot);
        return Optional.of(newSnapshot);
    }

    private void sendSnapshot(SensorsSnapshotAvro snapshot, String topic) {
        ProducerRecord<String, SensorsSnapshotAvro> record =
                new ProducerRecord<>(topic, snapshot.getHubId(), snapshot);

        producer.send(record, (RecordMetadata metadata, Exception exception) -> {
            if (exception != null) {
                log.error("Failed to send snapshot for hub {}: {}",
                        snapshot.getHubId(), exception.getMessage(), exception);
            } else {
                log.info("Snapshot successfully sent for hub {} to topic {}, partition {}, offset {}",
                        snapshot.getHubId(), metadata.topic(), metadata.partition(), metadata.offset());
            }
        });
    }

    private void shutdownGracefully() {
        try {
            log.info("Starting graceful shutdown...");

            // Перед тем, как закрыть продюсер и консьюмер, нужно убедится,
            // что все сообщения, лежащие в буффере, отправлены и
            // все оффсеты обработанных сообщений зафиксированы

            // Фиксируем все оффсеты
            try {
                if (consumer != null) {
                    consumer.commitSync();
                    log.info("All offsets committed");
                }
            } catch (Exception e) {
                log.error("Error committing offsets: {}", e.getMessage());
            }

            // Сбрасываем данные в буффере продюсера
            try {
                if (producer != null) {
                    producer.flush();
                    log.info("Producer buffer flushed");
                }
            } catch (Exception e) {
                log.error("Error flushing producer: {}", e.getMessage());
            }

        } finally {
            try {
                log.info("Closing consumer...");
                if (consumer != null) {
                    consumer.close(Duration.ofSeconds(5));
                }
            } catch (Exception e) {
                log.error("Error closing consumer: {}", e.getMessage());
            }

            try {
                log.info("Closing producer...");
                if (producer != null) {
                    producer.close(Duration.ofSeconds(5));
                }
            } catch (Exception e) {
                log.error("Error closing producer: {}", e.getMessage());
            }

            try {
                executorService.shutdown();
                if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }

            log.info("Aggregator stopped");
        }
    }

    public void stop() {
        if (running.compareAndSet(true, false)) {
            log.info("Stopping aggregator...");

            if (consumer != null) {
                consumer.wakeup();
            }

            if (mainThread != null) {
                mainThread.interrupt();
            }
        }
    }

    public boolean isRunning() {
        return running.get();
    }
}