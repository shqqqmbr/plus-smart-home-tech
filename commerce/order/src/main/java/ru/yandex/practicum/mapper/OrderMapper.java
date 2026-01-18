package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.model.Order;

public interface OrderMapper {
    OrderDto toDto(Order order);
    Order toEntity(OrderDto order);
}
