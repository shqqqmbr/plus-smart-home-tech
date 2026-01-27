package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;
import ru.yandex.practicum.constant.PaymentState;

import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "payment_id")
    UUID paymentId;

    @Column(name = "total_payment", nullable = false)
    double totalPayment;

    @Column(name = "delivery_total", nullable = false)
    double deliveryTotal;

    @Column(name = "fee_total", nullable = false)
    double feeTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_state", nullable = false)
    @Builder.Default
    PaymentState paymentState = PaymentState.PENDING;
}
