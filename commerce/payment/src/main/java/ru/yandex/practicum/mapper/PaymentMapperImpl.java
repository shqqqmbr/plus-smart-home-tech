package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.model.Payment;

@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public PaymentDto toDto(Payment payment) {
        return PaymentDto.builder()
                .paymentId(payment.getPaymentId().toString())
                .totalPayment(payment.getTotalPayment())
                .deliveryTotal(payment.getDeliveryTotal())
                .feeTotal(payment.getFeeTotal())
                .build();
    }

    @Override
    public Payment toEntity(PaymentDto paymentDto) {
        return Payment.builder()
                .paymentId(paymentDto.getPaymentId() != null ? java.util.UUID.fromString(paymentDto.getPaymentId()) : null)
                .totalPayment(paymentDto.getTotalPayment())
                .deliveryTotal(paymentDto.getDeliveryTotal())
                .feeTotal(paymentDto.getFeeTotal())
                .build();
    }
}
