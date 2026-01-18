package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;

public interface DeliveryService {
    DeliveryDto createDelivery(DeliveryDto deliveryDto);
    void successfulDelivery(String orderId);
    void pickedDelivery(String orderId);
    void failedDelivery(String orderId);
    double costOfDelivery(OrderDto order);
}
