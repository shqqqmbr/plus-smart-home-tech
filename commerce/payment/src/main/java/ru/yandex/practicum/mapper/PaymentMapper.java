package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.model.Payment;

public interface PaymentMapper {
    PaymentDto toDto(Payment payment);

    Payment toEntity(PaymentDto paymentDto);
}
