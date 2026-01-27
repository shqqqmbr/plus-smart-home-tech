package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.model.Delivery;

public interface DeliveryMapper {

    Delivery toEntity(DeliveryDto deliveryDto);

    DeliveryDto toDto(Delivery delivery);
}