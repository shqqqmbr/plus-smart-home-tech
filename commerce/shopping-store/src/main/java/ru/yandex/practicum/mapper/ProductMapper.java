package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.model.Product;

public interface ProductMapper {
    ProductDto toDto(Product product);

    Product toEntity(ProductDto productDto);
}


