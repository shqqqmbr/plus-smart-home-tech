package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;

import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderClient {

    @PostMapping("/assembly")
    OrderDto assembly(@RequestBody UUID orderId);

    @PostMapping("/delivery")
    OrderDto deliver(@RequestBody UUID orderId);

    @PostMapping("/payment")
    OrderDto pay(@RequestBody UUID orderId);

    @PostMapping("/completed")
    OrderDto complete(@RequestBody UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto abortDeliverByFail(@RequestBody UUID orderId);

    @PostMapping("/assembly/failed")
    OrderDto abortAssemblyByFail(@RequestBody UUID orderId);

    @PostMapping("/payment/failed")
    OrderDto abortOrderByPaymentFailed(@RequestBody UUID orderId);

    @PostMapping("/payment/success")
    void paymentSuccess(@RequestBody String paymentId);
}