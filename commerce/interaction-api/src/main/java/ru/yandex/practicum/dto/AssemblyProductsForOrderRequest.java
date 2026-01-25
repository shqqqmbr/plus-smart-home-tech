package ru.yandex.practicum.dto;

import java.util.Map;

public record AssemblyProductsForOrderRequest(
        String orderId,
        Map<String, Long> products
) {
}
