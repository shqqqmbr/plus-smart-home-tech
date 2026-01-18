package ru.yandex.practicum.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDto {
    String paymentId;
    double totalPayment;
    double deliveryTotal;
    double feeTotal;
}
