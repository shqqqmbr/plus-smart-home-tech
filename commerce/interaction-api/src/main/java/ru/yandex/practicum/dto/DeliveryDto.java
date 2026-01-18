package ru.yandex.practicum.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.constant.DeliveryState;

@Getter @Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryDto {
    String deliveryId;
    AddressDto fromAddress;
    AddressDto toAddress;
    String orderId;
    DeliveryState deliveryState;
}
