package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.service.DeliveryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery")
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PutMapping
    public DeliveryDto planDelivery(@RequestBody DeliveryDto deliveryDto) {
        return deliveryService.createDelivery(deliveryDto);
    }

    @PostMapping("/successful")
    public void deliverySuccessful(@RequestBody String orderId) {
        deliveryService.successfulDelivery(orderId);
    }

    @PostMapping("/picked")
    public void deliveryPicked(@RequestBody String orderId) {
        deliveryService.pickedDelivery(orderId);
    }

    @PostMapping("/failed")
    public void deliveryFailed(@RequestBody String orderId) {
        deliveryService.failedDelivery(orderId);
    }

    @PostMapping("/cost")
    public double deliveryCost(@RequestBody OrderDto orderDto) {
        return deliveryService.costOfDelivery(orderDto);
    }
}
