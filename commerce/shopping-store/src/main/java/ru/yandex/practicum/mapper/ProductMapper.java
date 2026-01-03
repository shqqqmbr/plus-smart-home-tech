package ru.yandex.practicum.mapper;

import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.model.ProductDto;

public interface ProductMapper {
    ProductDto toDto(Product product);

    Product toEntity(ProductDto productDto);
}


