package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record ProductReturnRequest(
        @NotBlank
        String orderId,
        @NotNull
        Map<String, Integer> products
) {
}
