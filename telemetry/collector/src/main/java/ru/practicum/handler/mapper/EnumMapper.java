package ru.practicum.handler.mapper;

import ru.practicum.constant.ActionType;
import ru.practicum.constant.ConditionType;
import ru.practicum.constant.DeviceType;
import ru.practicum.constant.OperationType;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.OperationTypeAvro;

public class EnumMapper {

    public static <T extends Enum<T>> T map(Enum<?> source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        if (targetClass == DeviceTypeAvro.class && source instanceof DeviceType) {
            return targetClass.cast(DeviceTypeAvro.valueOf(source.name()));
        }
        if (targetClass == DeviceType.class && source instanceof DeviceTypeAvro) {
            return targetClass.cast(DeviceType.valueOf(source.name()));
        }
        if (targetClass == ConditionTypeAvro.class && source instanceof ConditionType) {
            return targetClass.cast(ConditionTypeAvro.valueOf(source.name()));
        }
        if (targetClass == ConditionType.class && source instanceof ConditionTypeAvro) {
            return targetClass.cast(ConditionType.valueOf(source.name()));
        }
        if (targetClass == OperationTypeAvro.class && source instanceof OperationType) {
            return targetClass.cast(OperationTypeAvro.valueOf(source.name()));
        }
        if (targetClass == OperationType.class && source instanceof OperationTypeAvro) {
            return targetClass.cast(OperationType.valueOf(source.name()));
        }
        if (targetClass == ActionTypeAvro.class && source instanceof ActionType) {
            return targetClass.cast(ActionTypeAvro.valueOf(source.name()));
        }
        if (targetClass == ActionType.class && source instanceof ActionTypeAvro) {
            return targetClass.cast(ActionType.valueOf(source.name()));
        }
        throw new IllegalArgumentException(
                "Unsupported mapping from " + source.getClass().getSimpleName() +
                        " to " + targetClass.getSimpleName()
        );
    }
}