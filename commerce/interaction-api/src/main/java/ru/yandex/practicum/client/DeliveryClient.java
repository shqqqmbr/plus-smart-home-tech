package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;

import java.util.UUID;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryClient {

    @PutMapping
    DeliveryDto create(DeliveryDto deliveryDto);

    @PostMapping("/successful")
    void setDeliveryStatusSuccessful(@RequestBody String orderId);

    @PostMapping("/picked")
    void setDeliveryStatusPicked(@RequestBody String orderId);

    @PostMapping("/failed")
    void setFailedStatusToDelivery(@RequestBody String orderId);

    @PostMapping("/cost")
    Double calculateDeliveryCost(@RequestBody OrderDto orderDto);

}