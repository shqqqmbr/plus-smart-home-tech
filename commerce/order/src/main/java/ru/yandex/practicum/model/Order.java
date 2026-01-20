package ru.yandex.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;
import ru.yandex.practicum.constant.OrderState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "order_id")
    UUID orderId;

    @NotNull
    UUID shoppingCartId;

    @Column(name = "username")
    String username;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    @Builder.Default
    Map<UUID, Integer> products = new HashMap<>();

    UUID paymentId;
    UUID deliveryId;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    OrderState state = OrderState.NEW;

    double deliveryWeight;

    double deliveryVolume;

    boolean fragile;

    double totalPrice;

    double deliveryPrice;

    double productPrice;
}
