package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ProductReturnRequest;

import java.util.List;

public interface OrderService {
    List<OrderDto> getOrders(String username);
    OrderDto createOrder(CreateNewOrderRequest request);
    OrderDto returnOrder(ProductReturnRequest request);
    OrderDto paymentOrder(String orderId);
    OrderDto paymentOrderFailed(String orderId);
    OrderDto deliveryOrder(String orderId);
    OrderDto deliveryOrderFailed(String orderId);
    OrderDto completeOrder(String orderId);
    OrderDto calculateOrderCost(String orderId);
    OrderDto calculateDeliveryCost(String orderId);
    OrderDto assemblyOrder(String orderId);
    OrderDto assemblyOrderFailed(String orderId);
}
