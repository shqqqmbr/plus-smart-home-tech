package ru.practicum.handler.mapper;

import ru.practicum.constant.DeviceType;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

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
        throw new IllegalArgumentException(
            "Unsupported mapping from " + source.getClass().getSimpleName() + 
            " to " + targetClass.getSimpleName()
        );
    }
}