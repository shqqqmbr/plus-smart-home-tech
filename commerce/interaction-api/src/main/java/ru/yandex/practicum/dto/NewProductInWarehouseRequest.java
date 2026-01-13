package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;

public record NewProductInWarehouseRequest(
        @NotNull
        String productId,
        boolean fragile,
        @NotNull
        DimensionDto dimension,
        double weight
) {
}
