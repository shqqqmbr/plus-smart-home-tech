package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.constant.DeliveryState;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Delivery {
    @Id
    @Column(name = "delivery_id")
    String deliveryId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_address_id")
    Address fromAddress;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_address_id")
    Address toAddress;

    @Column(name = "order_id")
    String orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_state")
    DeliveryState deliveryState;
}
