package ru.yandex.practicum.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.constant.ProductCategory;
import ru.yandex.practicum.constant.QuantityState;
import ru.yandex.practicum.model.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts(ProductCategory productCategory, Pageable pageable);

    ProductDto getProductById(String id);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    boolean deleteProduct(String id);

    boolean updateStatus(String id, QuantityState state);

    ProductDto getProduct(String id);
}


