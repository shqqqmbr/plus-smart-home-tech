package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.CartProductId;
import ru.yandex.practicum.model.ShoppingCartDto;
import ru.yandex.practicum.service.CartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
@Validated
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ShoppingCartDto getCart(@RequestParam String username) {
        return cartService.getCart(username);
    }

    @PutMapping
    public ShoppingCartDto addProductToCart(@RequestParam String username, @RequestBody Map<String, Integer> productsIds) {
        return cartService.addProductToCart(username, productsIds);
    }

    @DeleteMapping
    public void deactivateCart(String username) {
        cartService.deactivateCart(username);
    }

    @PostMapping("/remove")
    public ShoppingCartDto removeProductFromCart(@RequestParam String username, @RequestBody List<UUID> productIds) {
        return cartService.removeProductFromCart(username, productIds);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeQuantity(@RequestParam String username, @RequestBody CartProductId product) {
        return cartService.changeQuantity(username, product);
    }
}
