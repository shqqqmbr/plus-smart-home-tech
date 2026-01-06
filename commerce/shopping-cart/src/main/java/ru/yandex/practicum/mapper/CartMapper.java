package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.model.CartProduct;
import ru.yandex.practicum.model.ShoppingCart;

import java.util.List;

public interface CartMapper {
    ShoppingCart toEntity(ShoppingCartDto dto, String username);
    ShoppingCartDto toDto(ShoppingCart cart, List<CartProduct> cartProducts);
}
