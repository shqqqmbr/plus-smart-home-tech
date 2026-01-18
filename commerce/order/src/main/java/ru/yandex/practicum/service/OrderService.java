package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PageResponse;
import ru.yandex.practicum.dto.ProductReturnRequest;

public interface OrderService {
    PageResponse<OrderDto> getOrders(String username, Integer page, Integer size, String sort);

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
