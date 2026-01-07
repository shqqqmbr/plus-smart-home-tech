package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter @Setter @ToString
@Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "reserved_products")
public class ReservedProduct {

    @Id
    @UuidGenerator
    @Column(name = "reserved_products_id")
    UUID reservedProductId;

    @Column(name = "shopping_cart_id", nullable = false)
    UUID shoppingCartId;

    @Column(name = "product_id", nullable = false)
    UUID productId;

    @Column(name = "reserved_quantity", nullable = false)
    long reservedQuantity;

}