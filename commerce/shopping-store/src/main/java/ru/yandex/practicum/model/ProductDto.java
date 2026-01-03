package ru.yandex.practicum.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.constant.ProductCategory;
import ru.yandex.practicum.constant.ProductState;
import ru.yandex.practicum.constant.QuantityState;

@Builder
@Data
public class ProductDto {
    private String productId;
    private String name;
    private String description;
    private String imageSrc;
    private QuantityState quantityState;
    private ProductState productState;
    private ProductCategory productCategory;
    private double price;
}
