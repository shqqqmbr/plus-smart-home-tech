package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.model.ProductDto;

import java.util.UUID;

@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDto toDto(Product product) {
        return ProductDto.builder()
                .productId(product.getProductId().toString())
                .name(product.getName())
                .description(product.getDescription())
                .imageSrc(product.getImageSrc())
                .quantityState(product.getQuantityState())
                .productState(product.getProductState())
                .productCategory(product.getProductCategory())
                .price(product.getPrice())
                .build();
    }

    @Override
    public Product toEntity(ProductDto productDto) {
        return Product.builder()
                .productId(convertToUuid(productDto.getProductId()))
                .name(productDto.getName())
                .description(productDto.getDescription())
                .imageSrc(productDto.getImageSrc())
                .quantityState(productDto.getQuantityState())
                .productState(productDto.getProductState())
                .productCategory(productDto.getProductCategory())
                .price(productDto.getPrice())
                .build();
    }

    private UUID convertToUuid(String uuidString) {
        if (uuidString == null || uuidString.trim().isEmpty()) {
            return null;
        }
        try {
            return UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            // Логируйте ошибку или обрабатывайте по вашему усмотрению
            throw new IllegalArgumentException("Invalid UUID format: " + uuidString, e);
        }
    }
}
