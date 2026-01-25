package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentClient {

    @PostMapping
    PaymentDto create(@RequestBody final OrderDto order);

    @PostMapping("/refund")
    ResponseEntity<Void> refund(@RequestBody String paymentId);

    @PostMapping("/failed")
    void paymentFailed(@RequestBody String paymentId);

    @PostMapping("/totalCost")
    Double calculateTotalCost(@RequestBody final OrderDto order);

    @PostMapping("/productCost")
    Double calculateProductCost(@RequestBody final OrderDto order);

}