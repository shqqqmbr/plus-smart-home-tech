package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "order_bookings")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservedProduct {

    @Id
    @UuidGenerator
    @Column(name = "reserved_product_id")
    private UUID reservedProductId;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "reserved_quantity", nullable = false)
    private long reservedQuantity;

}
