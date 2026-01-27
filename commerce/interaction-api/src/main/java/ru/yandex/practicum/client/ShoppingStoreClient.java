package ru.yandex.practicum.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.constant.ProductCategory;
import ru.yandex.practicum.constant.QuantityState;
import ru.yandex.practicum.dto.ProductDto;

import java.util.UUID;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient {
    @GetMapping
    Page<ProductDto> getProducts(@RequestParam ProductCategory category,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "150") int size,
                                 @RequestParam(required = false) String sort);

    @PutMapping
    ProductDto createProduct(@RequestBody @Valid ProductDto newProductDto);

    @PostMapping
    ProductDto updateProduct(@RequestBody @Valid ProductDto updateProductDto);

    @PostMapping("/removeProductFromStore")
    Boolean deleteProduct(@RequestBody @NotNull UUID productId);

    @PostMapping("/quantityState")
    Boolean updateStatus(@RequestParam @NotNull UUID productId,
                         @RequestParam @NotNull QuantityState quantityState);

    @GetMapping("/{productId}")
    ProductDto getProduct(@PathVariable @NotNull UUID productId);
}
