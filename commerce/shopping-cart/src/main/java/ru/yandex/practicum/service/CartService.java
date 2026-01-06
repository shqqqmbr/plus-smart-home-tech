package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.model.CartProductId;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CartService {
    ShoppingCartDto getCart(String username);

    ShoppingCartDto addProductToCart(String username, Map<String, Integer> productIds);

    void deactivateCart(String username);

    ShoppingCartDto removeProductFromCart(String username, List<UUID> productIds);

    ShoppingCartDto changeQuantity(String username, CartProductId product);
}
