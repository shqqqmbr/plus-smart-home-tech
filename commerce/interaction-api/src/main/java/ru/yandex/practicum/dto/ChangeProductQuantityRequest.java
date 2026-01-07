package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record ChangeProductQuantityRequest(

    @NotBlank
    String productId,

    @PositiveOrZero
    long newQuantity
) {
}