package ru.yandex.practicum.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.constant.OrderState;

import java.util.Map;

@Data
@Builder
public class OrderDto {
    String orderId;
    String shoppingCartId;
    Map<String, Integer> products;
    String paymentId;
    String deliveryId;
    OrderState state;
    double deliveryWeight;
    double deliveryVolume;
    boolean fragile;
    double totalPrice;
    double deliveryPrice;
    double productPrice;
}
