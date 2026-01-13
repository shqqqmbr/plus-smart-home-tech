package ru.yandex.practicum.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;

import java.util.Map;
import java.util.Set;

@FeignClient(name = "shopping-cart")
@RequestMapping("/api/v1/shopping-cart")
public interface ShoppingCartClient {

    @GetMapping
    ShoppingCartDto getCart(@RequestParam String username);

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
