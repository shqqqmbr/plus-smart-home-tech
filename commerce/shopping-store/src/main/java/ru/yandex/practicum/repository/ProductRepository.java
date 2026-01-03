package ru.yandex.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.constant.ProductCategory;
import ru.yandex.practicum.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByProductCategory(ProductCategory productCategory, Pageable pageable);
}


