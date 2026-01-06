package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.constant.ProductCategory;
import ru.yandex.practicum.constant.QuantityState;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    @Override
    public List<ProductDto> getAllProducts(ProductCategory productCategory, Pageable pageable) {
        return productRepository.findByProductCategory(productCategory, pageable).stream()
                .map(productMapper::toDto)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public ProductDto getProductById(String id) {
        return productMapper.toDto(productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product with id=" + id + " not found")));
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        return productMapper.toDto(productRepository.save(productMapper.toEntity(productDto)));
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        productRepository.findById(productDto.getProductId()).orElseThrow(() -> new NotFoundException("Product with id=" + productDto.getProductId() + " not found"));
        productRepository.deleteById(productDto.getProductId());
        return productMapper.toDto(productRepository.save(productMapper.toEntity(productDto)));
    }

    @Override
    public boolean deleteProduct(String id) {
        productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product with id=" + id + " not found"));
        try {
            productRepository.deleteById(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateStatus(String id, QuantityState state) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product with id=" + id + " not found"));
        product.setQuantityState(state);
        try {
            productRepository.save(product);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public ProductDto getProduct(String id) {
        return productMapper.toDto(productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product with id=" + id + " not found")));
    }
}
