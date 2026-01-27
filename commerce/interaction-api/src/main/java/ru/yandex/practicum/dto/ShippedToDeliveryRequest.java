package ru.yandex.practicum.dto;

public record ShippedToDeliveryRequest(
        String orderId,
        String deliveryId
) {
}
