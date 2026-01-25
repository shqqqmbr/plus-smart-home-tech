package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.Delivery;

public interface DeliveryRespoitory extends JpaRepository<Delivery, String> {
    Delivery findByOrderId(String orderId);
}
