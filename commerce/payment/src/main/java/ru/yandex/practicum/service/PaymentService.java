package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

public interface PaymentService {
    double calculateProductCost(OrderDto orderDto);

    double calculateTotalCost(OrderDto orderDto);

    PaymentDto createPayment(OrderDto orderDto);

    void paymentSuccess(String paymentId);

    void paymentFailed(String paymentId);
}
