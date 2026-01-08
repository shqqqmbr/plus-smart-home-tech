package ru.yandex.practicum.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.constant.ProductCategory;
import ru.yandex.practicum.constant.QuantityState;
import ru.yandex.practicum.dto.PageResponse;
import ru.yandex.practicum.dto.ProductDto;

import java.util.UUID;

public interface ProductService {
    PageResponse<ProductDto> getAllProducts(ProductCategory productCategory, Pageable pageable);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    boolean deleteProduct(UUID id);

    boolean updateStatus(UUID id, QuantityState quantityState);

    ProductDto getProduct(String id);
}


