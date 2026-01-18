package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.client.ShoppingStoreClient;
import ru.yandex.practicum.constant.PaymentState;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.repository.PaymentRepository;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;

    @Override
    public double calculateProductCost(OrderDto orderDto) {
        if (orderDto == null || orderDto.getProducts() == null || orderDto.getProducts().isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException("Недостаточно информации в заказе для расчёта");
        }

        double totalCost = 0.0;
        for (Map.Entry<String, Integer> entry : orderDto.getProducts().entrySet()) {
            UUID productId = UUID.fromString(entry.getKey());
            int quantity = entry.getValue();

            ProductDto product = shoppingStoreClient.getProduct(productId);
            totalCost += product.getPrice() * quantity;
        }

        return totalCost;
    }

    @Override
    public double calculateTotalCost(OrderDto orderDto) {
        if (orderDto == null || orderDto.getProductPrice() == 0 || orderDto.getDeliveryPrice() == 0) {
            throw new NotEnoughInfoInOrderToCalculateException("Недостаточно информации в заказе для расчёта");
        }

        double productCost = orderDto.getProductPrice();
        double vat = productCost * 0.1;
        double totalWithVat = productCost + vat;
        double deliveryCost = orderDto.getDeliveryPrice();
        double totalCost = totalWithVat + deliveryCost;

        return totalCost;
    }

    @Override
    public PaymentDto createPayment(OrderDto orderDto) {
        if (orderDto == null || orderDto.getProductPrice() == 0 || orderDto.getDeliveryPrice() == 0) {
            throw new NotEnoughInfoInOrderToCalculateException("Недостаточно информации в заказе для расчёта");
        }

        double productCost = orderDto.getProductPrice();
        double vat = productCost * 0.1;
        double deliveryCost = orderDto.getDeliveryPrice();
        double totalCost = productCost + vat + deliveryCost;

        Payment payment = Payment.builder()
                .totalPayment(totalCost)
                .deliveryTotal(deliveryCost)
                .feeTotal(vat)
                .paymentState(PaymentState.PENDING)
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toDto(savedPayment);
    }

    @Override
    public void paymentSuccess(String paymentId) {
        Payment payment = paymentRepository.findById(UUID.fromString(paymentId))
                .orElseThrow(() -> new NotFoundException("Payment not found: " + paymentId));

        payment.setPaymentState(PaymentState.SUCCESS);
        paymentRepository.save(payment);

        orderClient.paymentSuccess(paymentId);
    }

    @Override
    public void paymentFailed(String paymentId) {
        Payment payment = paymentRepository.findById(UUID.fromString(paymentId))
                .orElseThrow(() -> new NotFoundException("Payment not found: " + paymentId));

        payment.setPaymentState(PaymentState.FAILED);
        paymentRepository.save(payment);
    }
}
