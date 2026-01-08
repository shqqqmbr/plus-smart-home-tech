package ru.yandex.practicum.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.constant.ProductCategory;
import ru.yandex.practicum.constant.QuantityState;
import ru.yandex.practicum.dto.ProductDto;

import java.util.UUID;

public interface ShoppingStoreClient {
    Page<ProductDto> getProducts(@RequestParam ProductCategory category, Pageable pageable);

    ProductDto createProduct(@RequestBody @Valid ProductDto newProductDto);

    ProductDto updateProduct(@RequestBody @Valid ProductDto updateProductDto);

    Boolean deleteProduct(@RequestBody @NotNull UUID productId);

    Boolean updateQuantityState(@RequestParam @NotNull UUID productId,
                                @RequestParam @NotNull QuantityState quantityState);

    ProductDto getProductById(@PathVariable @NotNull UUID productId);
}
