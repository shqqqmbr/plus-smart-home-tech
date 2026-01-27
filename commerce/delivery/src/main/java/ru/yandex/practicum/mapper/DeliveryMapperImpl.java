package ru.yandex.practicum.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.model.Delivery;

@Component
@RequiredArgsConstructor
public class DeliveryMapperImpl implements DeliveryMapper {

    private final AddressMapper addressMapper;

    @Override
    public Delivery toEntity(DeliveryDto deliveryDto) {
        return Delivery.builder()
                .deliveryId(deliveryDto.getDeliveryId())
                .fromAddress(addressMapper.toEntity(deliveryDto.getFromAddress()))
                .toAddress(addressMapper.toEntity(deliveryDto.getToAddress()))
                .orderId(deliveryDto.getOrderId())
                .deliveryState(deliveryDto.getDeliveryState())
                .build();
    }

    @Override
    public DeliveryDto toDto(Delivery delivery) {
        return DeliveryDto.builder()
                .deliveryId(delivery.getDeliveryId())
                .fromAddress(addressMapper.toDto(delivery.getFromAddress()))
                .toAddress(addressMapper.toDto(delivery.getToAddress()))
                .orderId(delivery.getOrderId())
                .deliveryState(delivery.getDeliveryState())
                .build();
    }
}