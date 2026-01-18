package ru.yandex.practicum.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.constant.OrderState;

import java.util.Map;

@Getter @Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
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
