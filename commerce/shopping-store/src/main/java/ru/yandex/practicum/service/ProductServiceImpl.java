package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.constant.ProductCategory;
import ru.yandex.practicum.constant.ProductState;
import ru.yandex.practicum.constant.QuantityState;
import ru.yandex.practicum.dto.PageResponse;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ProductRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    @Override
    public PageResponse<ProductDto> getAllProducts(ProductCategory productCategory, Pageable pageable) {
        Page<Product> productPage = productRepository.findAllByProductCategory(productCategory, pageable);
        Page<ProductDto> dtoPage = productPage.map(productMapper::toDto);
        return PageResponse.fromPage(dtoPage);
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product productSaved = productRepository.save(productMapper.toEntity(productDto));
        return productMapper.toDto(productSaved);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        productRepository.findById(productDto.getProductId()).orElseThrow(() -> new NotFoundException("Product with id=" + productDto.getProductId() + " not found"));
        productRepository.deleteById(productDto.getProductId());
        return productMapper.toDto(productRepository.save(productMapper.toEntity(productDto)));
    }

    @Override
    public boolean deleteProduct(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product with id=" + id + " not found"));
        product.setProductState(ProductState.DEACTIVATE);
        productRepository.save(product);
        return true;
    }

    @Override
    public boolean updateStatus(UUID id, QuantityState quantityState) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product with id=" + id + " not found"));
        product.setQuantityState(quantityState);
        productRepository.save(product);
        return true;
    }

    @Override
    public ProductDto getProduct(String id) {
        UUID productId = UUID.fromString(id);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product with id=" + id + " not found"));
        return productMapper.toDto(product);
    }
}
