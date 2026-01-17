package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ProductReturnRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    @Override
    public List<OrderDto> getOrders(String username) {
        return List.of();
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest request) {
        return null;
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest request) {
        return null;
    }

    @Override
    public OrderDto paymentOrder(String orderId) {
        return null;
    }

    @Override
    public OrderDto paymentOrderFailed(String orderId) {
        return null;
    }

    @Override
    public OrderDto deliveryOrder(String orderId) {
        return null;
    }

    @Override
    public OrderDto deliveryOrderFailed(String orderId) {
        return null;
    }

    @Override
    public OrderDto completeOrder(String orderId) {
        return null;
    }

    @Override
    public OrderDto calculateOrderCost(String orderId) {
        return null;
    }

    @Override
    public OrderDto calculateDeliveryCost(String orderId) {
        return null;
    }

    @Override
    public OrderDto assemblyOrder(String orderId) {
        return null;
    }

    @Override
    public OrderDto assemblyOrderFailed(String orderId) {
        return null;
    }
}
