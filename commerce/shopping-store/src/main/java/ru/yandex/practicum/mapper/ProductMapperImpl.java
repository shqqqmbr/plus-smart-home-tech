package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.model.Product;

import java.util.UUID;

@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDto toDto(Product product) {
        return ProductDto.builder()
                .productName(product.getProductName())
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
        UUID productId = generateProductId(productDto);
        return Product.builder()
                .productId(productId)
                .productName(productDto.getProductName())
                .description(productDto.getDescription())
                .imageSrc(productDto.getImageSrc())
                .quantityState(productDto.getQuantityState())
                .productState(productDto.getProductState())
                .productCategory(productDto.getProductCategory())
                .price(productDto.getPrice())
                .build();
    }

    private UUID generateProductId(ProductDto productDto) {
        if (productDto.getProductId() != null &&
                !productDto.getProductId().trim().isEmpty()) {
            try {
                return UUID.fromString(productDto.getProductId());
            } catch (IllegalArgumentException e) {
                return UUID.randomUUID();
            }
        }
        return UUID.randomUUID();
    }
}
