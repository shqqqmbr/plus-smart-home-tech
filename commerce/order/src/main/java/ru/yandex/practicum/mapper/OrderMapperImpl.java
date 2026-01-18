package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.model.Order;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderMapperImpl implements OrderMapper {
    @Override
    public OrderDto toDto(Order order) {
        Map<String, Integer> productsForDto = order.getProducts().entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().toString(),
                        Map.Entry::getValue
                ));
        return OrderDto.builder()
                .orderId(order.getOrderId() != null ? order.getOrderId().toString() : null)
                .shoppingCartId(order.getShoppingCartId() != null ? order.getShoppingCartId().toString() : null)
                .products(productsForDto)
                .paymentId(order.getPaymentId() != null ? order.getPaymentId().toString() : null)
                .deliveryId(order.getDeliveryId() != null ? order.getDeliveryId().toString() : null)
                .state(order.getState())
                .deliveryWeight(order.getDeliveryWeight())
                .deliveryVolume(order.getDeliveryVolume())
                .fragile(order.isFragile())
                .totalPrice(order.getTotalPrice())
                .deliveryPrice(order.getDeliveryPrice())
                .productPrice(order.getProductPrice())
                .build();
    }

    @Override
    public Order toEntity(OrderDto dto) {
        Map<UUID, Integer> productsForEnt = dto.getProducts().entrySet().stream()
                .collect(Collectors.toMap(
                        e -> UUID.fromString(e.getKey()),
                        Map.Entry::getValue
                ));
        return Order.builder()
                .orderId(dto.getOrderId() != null ? UUID.fromString(dto.getOrderId()) : null)
                .shoppingCartId(dto.getShoppingCartId() != null ? UUID.fromString(dto.getShoppingCartId()) : null)
                .products(productsForEnt)
                .paymentId(dto.getPaymentId() != null ? UUID.fromString(dto.getPaymentId()) : null)
                .deliveryId(dto.getDeliveryId() != null ? UUID.fromString(dto.getDeliveryId()) : null)
                .state(dto.getState())
                .deliveryWeight(dto.getDeliveryWeight())
                .deliveryVolume(dto.getDeliveryVolume())
                .fragile(dto.isFragile())
                .totalPrice(dto.getTotalPrice())
                .deliveryPrice(dto.getDeliveryPrice())
                .productPrice(dto.getProductPrice())
                .build();
    }
}
