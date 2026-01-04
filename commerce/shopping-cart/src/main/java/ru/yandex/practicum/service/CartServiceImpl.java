package ru.yandex.practicum.service;

import jakarta.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.mapper.CartMapper;
import ru.yandex.practicum.model.CartProduct;
import ru.yandex.practicum.model.CartProductId;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.model.ShoppingCartDto;
import ru.yandex.practicum.repository.CartProductsRepository;
import ru.yandex.practicum.repository.ShoppingCartRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CartServiceImpl implements CartService {
    private final CartProductsRepository cartProductsRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartMapper cartMapper;

    @Override
    public ShoppingCartDto getCart(String username) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUsernameIgnoreCaseAndActivated(username, true);
        if (shoppingCart == null) {
            throw new NotAuthorizedException("User " + username + "does not have cart");
        }
        List<CartProduct> cartProductList = cartProductsRepository.findAllByCartProductId_ShoppingCartId(shoppingCart.getShoppingCartId());
        return cartMapper.toDto(shoppingCart, cartProductList);
    }

    @Override
    public ShoppingCartDto addProductToCart(String username, Map<String, Integer> productIds) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUsernameIgnoreCaseAndActivated(username, true);
        if (shoppingCart == null) {
            throw new NotAuthorizedException("User " + username + "does not have cart");
        }
        UUID shoppingCartId = shoppingCart.getShoppingCartId();
        List<CartProduct> newCartProducts = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : productIds.entrySet()) {
            newCartProducts.add(
                    new CartProduct(
                            new CartProductId(shoppingCartId, UUID.fromString(entry.getKey())), entry.getValue()));
        }

        cartProductsRepository.saveAll(newCartProducts);
        return getCart(username);
    }

    @Override
    public void deactivateCart(String username) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUsernameIgnoreCaseAndActivated(username, true);
        if (shoppingCart == null) {
            throw new NotAuthorizedException("User " + username + "does not have cart");
        }
        shoppingCart.setActivated(false);
    }

    @Override
    public ShoppingCartDto removeProductFromCart(String username, List<UUID> productIds) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUsernameIgnoreCaseAndActivated(username, true);
        if (shoppingCart == null) {
            throw new NotAuthorizedException("User " + username + "does not have cart");
        }
        UUID shoppingCartId = shoppingCart.getShoppingCartId();
        for (UUID productId : productIds) {
            CartProductId cartProduct = new CartProductId(shoppingCartId, productId);
            cartProductsRepository.findById(cartProduct)
                    .ifPresent(cartProductsRepository::delete);
        }

        List<CartProduct> remainingItems = cartProductsRepository.findAllByCartProductId_ShoppingCartId(shoppingCartId);

        if (remainingItems.isEmpty()) {
            shoppingCart.setActivated(false);
            shoppingCartRepository.save(shoppingCart);
        }

        return getCart(shoppingCart.getUsername());
    }

    @Override
    public ShoppingCartDto changeQuantity(String username, CartProductId product) {
        return null;
    }

    private Map<CartProductId, Integer> convertToMap(List<CartProduct> cartProducts) {
        return cartProducts.stream()
                .collect(Collectors.toMap(
                        CartProduct::getCartProductId,
                        CartProduct::getQuantity
                ));
    }
}
