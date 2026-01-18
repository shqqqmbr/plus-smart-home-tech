package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.service.PaymentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/productCost")
    public double productCost(@RequestBody OrderDto orderDto) {
        return paymentService.calculateProductCost(orderDto);
    }

    @PostMapping("/totalCost")
    public double getTotalCost(@RequestBody OrderDto orderDto) {
        return paymentService.calculateTotalCost(orderDto);
    }

    @PostMapping
    public PaymentDto payment(@RequestBody OrderDto orderDto) {
        return paymentService.createPayment(orderDto);
    }

    @PostMapping("/refund")
    public void paymentSuccess(@RequestBody String paymentId) {
        paymentService.paymentSuccess(paymentId);
    }

    @PostMapping("/failed")
    public void paymentFailed(@RequestBody String paymentId) {
        paymentService.paymentFailed(paymentId);
    }
}
