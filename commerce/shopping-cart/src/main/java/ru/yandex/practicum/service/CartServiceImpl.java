package ru.yandex.practicum.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.exception.NotAuthorizedException;
import ru.yandex.practicum.mapper.CartMapper;
import ru.yandex.practicum.model.CartProduct;
import ru.yandex.practicum.model.CartProductId;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.repository.CartProductsRepository;
import ru.yandex.practicum.repository.ShoppingCartRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CartServiceImpl implements CartService {
    private final CartProductsRepository cartProductsRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartMapper cartMapper;

    @Override
    public ShoppingCartDto createCart(String username) {
        ShoppingCart existingCart = shoppingCartRepository.findByUsernameIgnoreCaseAndActivated(username, true);
        if (existingCart != null) {
            return cartMapper.toDto(existingCart, new ArrayList<>());
        }

        ShoppingCart newCart = new ShoppingCart();
        newCart.setUsername(username);
        newCart.setActivated(true);
        newCart = shoppingCartRepository.save(newCart);

        return cartMapper.toDto(newCart, new ArrayList<>());
    }

    @Override
    public ShoppingCartDto getCart(String username) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUsernameIgnoreCaseAndActivated(username, true);
        if (shoppingCart == null) {
            return createCart(username);
        }
        List<CartProduct> cartProductList = cartProductsRepository.findAllByCartProductId_ShoppingCartId(shoppingCart.getShoppingCartId());
        if (cartProductList.isEmpty()) {
            ShoppingCartDto dto = cartMapper.toDto(shoppingCart, new ArrayList<>());
            dto.setProducts(new HashMap<>());
            return dto;
        }
        Map<String, Integer> productsMap = new LinkedHashMap<>();
        int counter = 1;

        for (CartProduct cartProduct : cartProductList) {
            String key = "additionalProp" + counter;
            productsMap.put(key, cartProduct.getQuantity());
            counter++;
        }
        return cartMapper.toDto(shoppingCart, cartProductList);
    }

    @Override
    @Transactional
    public ShoppingCartDto addProductToCart(String username, Map<String, Integer> productIds) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUsernameIgnoreCaseAndActivated(username, true);
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            shoppingCart.setUsername(username);
            shoppingCart.setActivated(true);
            shoppingCart = shoppingCartRepository.saveAndFlush(shoppingCart);
        }
        UUID shoppingCartId = shoppingCart.getShoppingCartId();
        List<CartProduct> newCartProducts = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : productIds.entrySet()) {
            CartProductId cartProductId = new CartProductId(
                    UUID.fromString(entry.getKey()),
                    shoppingCartId
            );
            CartProduct cartProduct = new CartProduct(cartProductId, entry.getValue());
            newCartProducts.add(cartProduct);
        }

        cartProductsRepository.saveAllAndFlush(newCartProducts);
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
    @Transactional
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request) {
        ShoppingCart cart = shoppingCartRepository.findByUsernameIgnoreCaseAndActivated(username, true);
        CartProductId fullId = new CartProductId(
                UUID.fromString(request.productId()),
                cart.getShoppingCartId()
        );
        CartProduct cartProduct = cartProductsRepository.findById(fullId).get();
        if (request.newQuantity() == 0) {
            cartProductsRepository.delete(cartProduct);
        } else {
            cartProduct.setQuantity((int) request.newQuantity());
            cartProductsRepository.save(cartProduct);
        }
        return getCart(username);
    }

    private Map<CartProductId, Integer> convertToMap(List<CartProduct> cartProducts) {
        return cartProducts.stream()
                .collect(Collectors.toMap(
                        CartProduct::getCartProductId,
                        CartProduct::getQuantity
                ));
    }
}
