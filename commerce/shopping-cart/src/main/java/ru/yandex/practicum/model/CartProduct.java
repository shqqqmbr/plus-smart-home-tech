package ru.yandex.practicum.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@Builder
@Table(name = "carts_products")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartProduct {
    @EmbeddedId
    private CartProductId cartProductId;
    private Integer quantity;
}
