package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.model.CartProduct;
import ru.yandex.practicum.model.ShoppingCart;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CartMapperImpl implements CartMapper {

    @Override
    public ShoppingCart toEntity(ShoppingCartDto dto, String username) {
        return ShoppingCart.builder()
                .shoppingCartId(convertToUuid(dto.getShoppingCartId()))
                .username(username)
                .build();
    }

    @Override
    public ShoppingCartDto toDto(ShoppingCart cart, List<CartProduct> cartProducts) {
        Map<String, Integer> productsMap = cartProducts.stream()
                .collect(Collectors.toMap(cartProduct -> cartProduct.getCartProductId().getProductId().toString(), CartProduct::getQuantity));
        return ShoppingCartDto.builder()
                .shoppingCartId(cart.getShoppingCartId().toString())
                .products(productsMap)
                .build();
    }

    private UUID convertToUuid(String uuidString) {
        if (uuidString == null || uuidString.trim().isEmpty()) {
            return null;
        }
        try {
            return UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format: " + uuidString, e);
        }
    }
}
