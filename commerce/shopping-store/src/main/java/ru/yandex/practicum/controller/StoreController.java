package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.constant.ProductCategory;
import ru.yandex.practicum.constant.QuantityState;
import ru.yandex.practicum.model.ProductDto;
import ru.yandex.practicum.service.ProductServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
@Validated
public class StoreController {
    ProductServiceImpl productServiceImpl;

    @GetMapping
    public List<ProductDto> getProducts(@RequestParam ProductCategory category, Pageable pageable) {
        return productServiceImpl.getAllProducts(category, pageable);
    }

    @PutMapping
    public ProductDto createProduct(@RequestBody ProductDto product) {
        return productServiceImpl.createProduct(product);
    }

    @PostMapping
    public ProductDto updateProduct(ProductDto product) {
        return productServiceImpl.updateProduct(product);
    }

    @PostMapping("/removeProductFromStore")
    public boolean deleteProduct(String id) {
        return productServiceImpl.deleteProduct(id);
    }

    @PostMapping("/quantityState")
    public boolean updateStatus(String id, QuantityState state) {
        return productServiceImpl.updateStatus(id, state);
    }

    @GetMapping("/{productId}")
    public ProductDto getProduct(String id) {
        return productServiceImpl.getProduct(id);
    }

}
