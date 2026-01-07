package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
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
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "shopping_cart_id")
    private UUID shoppingCartId;

    @Column(name = "username")
    private String username;

    @Column(name = "activated")
    private boolean activated;

}
