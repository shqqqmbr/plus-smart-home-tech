package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.ReservedProduct;

import java.util.List;
import java.util.UUID;

public interface ReservedProductRepository extends JpaRepository<ReservedProduct, UUID> {
    List<ReservedProduct> findByOrderIdAndDeliveryId(UUID orderId, UUID deliveryId);
}
