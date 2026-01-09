package ru.yandex.practicum.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddProductToWarehouseRequest(
        @NotNull
        String productId,

        @Min(1)
        int quantity
) {
}
