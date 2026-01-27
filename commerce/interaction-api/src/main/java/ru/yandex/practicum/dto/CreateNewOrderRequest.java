package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;

public record CreateNewOrderRequest(
        @NotNull
        ShoppingCartDto shoppingCart,
        @NotNull
        AddressDto deliveryAddress
) {
}
