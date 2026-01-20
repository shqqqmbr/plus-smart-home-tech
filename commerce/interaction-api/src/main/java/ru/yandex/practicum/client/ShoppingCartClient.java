package ru.yandex.practicum.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface ShoppingCartClient {

    @GetMapping
    ShoppingCartDto getCart(@RequestParam String username);

    @GetMapping("/{shoppingCartId}")
    ShoppingCartDto getCartById(@PathVariable UUID shoppingCartId);

    @PutMapping
    ShoppingCartDto addProductToCart(@RequestParam String username,
                                     @RequestBody @NotNull Map<String, Integer> products);

    @DeleteMapping
    void deactivateCart(@RequestParam String username);

    @PostMapping("/remove")
    ShoppingCartDto removeProductFromCart(@RequestParam String username, @RequestBody Set<String> products);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeQuantity(@RequestParam String username,
                                   @RequestBody @Valid ChangeProductQuantityRequest request);
}
