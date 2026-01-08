package ru.yandex.practicum.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ShoppingCartClient {
    ShoppingCartDto getCart(@RequestParam String username);

    ShoppingCartDto addProduct(@RequestParam String username,
                               @RequestBody @NotNull Map<UUID, Long> products);

    void deactivateCart(@RequestParam String username);

    ShoppingCartDto deleteProduct(@RequestParam String username, @RequestBody Set<UUID> products);

    ShoppingCartDto updateProductQuantity(@RequestParam String username,
                                          @RequestBody @Valid ChangeProductQuantityRequest request);
}
