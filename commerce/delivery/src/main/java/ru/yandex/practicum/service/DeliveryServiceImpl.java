package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.constant.DeliveryState;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.exception.NoDeliveryFoundException;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.repository.DeliveryRespoitory;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRespoitory deliveryRespoitory;
    private final DeliveryMapper deliveryMapper;
    private final WarehouseClient warehouseClient;
    private final OrderClient orderClient;

    @Override
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        if (deliveryDto.getDeliveryId() == null) {
            deliveryDto.setDeliveryId(UUID.randomUUID().toString());
        }
        Delivery delivery = deliveryRespoitory.save(deliveryMapper.toEntity(deliveryDto));
        return deliveryMapper.toDto(delivery);
    }

    @Override
    public void successfulDelivery(String orderId) {
        Delivery delivery = deliveryRespoitory.findByOrderId(orderId);
        if (delivery == null) {
            throw new NoDeliveryFoundException("Delivery not found for order: " + orderId);
        }
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        deliveryRespoitory.save(delivery);
        orderClient.deliver(UUID.fromString(orderId));
    }

    @Override
    public void pickedDelivery(String orderId) {
        Delivery delivery = deliveryRespoitory.findByOrderId(orderId);
        if (delivery == null) {
            throw new NoDeliveryFoundException("Delivery not found for order: " + orderId);
        }
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        deliveryRespoitory.save(delivery);
        orderClient.assembly(UUID.fromString(orderId));
        warehouseClient.shippedToDelivery(orderId);
    }

    @Override
    public void failedDelivery(String orderId) {
        Delivery delivery = deliveryRespoitory.findByOrderId(orderId);
        if (delivery == null) {
            throw new NoDeliveryFoundException("Delivery not found for order: " + orderId);
        }
        delivery.setDeliveryState(DeliveryState.FAILED);
        deliveryRespoitory.save(delivery);
        orderClient.abortDeliverByFail(UUID.fromString(orderId));
    }

    @Override
    public double costOfDelivery(OrderDto order) {
        AddressDto warehouseAddress = warehouseClient.getAddress();
        double baseCost = 5.0;

        double cost = baseCost;
        String warehouseStreet = warehouseAddress.getStreet();
        if (warehouseStreet != null) {
            if (warehouseStreet.contains("ADDRESS_2")) {
                cost = baseCost * 2 + baseCost;
            } else if (warehouseStreet.contains("ADDRESS_1")) {
                cost = baseCost * 1;
            }
        }

        if (order.isFragile()) {
            cost = cost + cost * 0.2;
        }

        cost = cost + order.getDeliveryWeight() * 0.3;

        cost = cost + order.getDeliveryVolume() * 0.2;

        if (order.getDeliveryId() != null) {
            Delivery delivery = deliveryRespoitory.findByOrderId(order.getOrderId());
            if (delivery != null && delivery.getToAddress() != null) {
                String deliveryStreet = delivery.getToAddress().getStreet();
                if (deliveryStreet != null && !deliveryStreet.equals(warehouseStreet)) {
                    cost = cost + cost * 0.2;
                }
            }
        }

        return cost;
    }
}
