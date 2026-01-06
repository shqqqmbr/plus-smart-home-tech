package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Data
@Builder
@Table(name = "shopping_carts")
@AllArgsConstructor
@NoArgsConstructor

public class ShoppingCart {
    @Id
    @Column
    @UuidGenerator
    private UUID shoppingCartId;

    private String username;

    private boolean activated;

}
