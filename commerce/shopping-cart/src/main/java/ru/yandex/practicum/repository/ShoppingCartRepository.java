package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.ShoppingCartApp;
import ru.yandex.practicum.model.ShoppingCart;

import java.util.UUID;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID> {
    ShoppingCart findByUsernameIgnoreCaseAndActivated(String username, Boolean activated);

}
