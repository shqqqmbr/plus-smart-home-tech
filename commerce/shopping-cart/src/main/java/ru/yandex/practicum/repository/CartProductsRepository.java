package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.CartProduct;
import ru.yandex.practicum.model.CartProductId;
import ru.yandex.practicum.model.ShoppingCart;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartProductsRepository extends JpaRepository<CartProduct, CartProductId> {
    List<CartProduct> findAllByCartProductId_ShoppingCartId(UUID shoppingCartId);
}
