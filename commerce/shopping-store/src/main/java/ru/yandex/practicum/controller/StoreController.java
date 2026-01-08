package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.constant.ProductCategory;
import ru.yandex.practicum.constant.QuantityState;
import ru.yandex.practicum.dto.PageResponse;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.service.ProductService;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
@Validated
public class StoreController {
    private final ProductService productService;

    @GetMapping
    public PageResponse<ProductDto> getProducts(@RequestParam ProductCategory category, Pageable pageable) {
        return productService.getAllProducts(category, pageable);
    }

    @PutMapping
    public ProductDto createProduct(@RequestBody ProductDto product) {
        return productService.createProduct(product);
    }

    @PostMapping
    public ProductDto updateProduct(@RequestBody ProductDto product) {
        return productService.updateProduct(product);
    }

    @PostMapping("/removeProductFromStore")
    public boolean deleteProduct(@RequestBody UUID productId) {
        return productService.deleteProduct(productId);
    }

    @PostMapping("/quantityState")
    public boolean updateStatus(@RequestParam UUID productId, @RequestParam QuantityState quantityState) {
        return productService.updateStatus(productId, quantityState);
    }

    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable String productId) {
        return productService.getProduct(productId);
    }

}
