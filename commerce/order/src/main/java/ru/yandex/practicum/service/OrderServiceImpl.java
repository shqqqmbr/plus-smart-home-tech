package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.client.DeliveryClient;
import ru.yandex.practicum.client.PaymentClient;
import ru.yandex.practicum.client.ShoppingCartClient;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.constant.OrderState;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NoOrderFoundException;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.repository.OrderRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartClient shoppingCartClient;
    private final WarehouseClient warehouseClient;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;

    @Override
    public List<OrderDto> getOrders(String username) {
        return orderRepository.findAllByUsername(username).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest request) {
        ShoppingCartDto cart = request.shoppingCart();
        BookedProductsDto bookedProducts = warehouseClient.checkQuantity(cart);

        String username = cart.getUsername();
        if ((username == null || username.isBlank())
                && cart.getShoppingCartId() != null
                && !cart.getShoppingCartId().isBlank()) {
            try {
                ShoppingCartDto cartFromService = shoppingCartClient.getCartById(UUID.fromString(cart.getShoppingCartId()));
                username = cartFromService != null ? cartFromService.getUsername() : null;
            } catch (Exception ignored) {
            }
        }

        Map<UUID, Long> productsMap = cart.getProducts().entrySet().stream()
                .collect(Collectors.toMap(
                        e -> UUID.fromString(e.getKey()),
                        Map.Entry::getValue
                ));

        Order order = Order.builder()
                .shoppingCartId(UUID.fromString(cart.getShoppingCartId()))
                .username(username)
                .products(productsMap)
                .state(OrderState.NEW)
                .deliveryWeight(bookedProducts.getDeliveryWeight())
                .deliveryVolume(bookedProducts.getDeliveryVolume())
                .fragile(bookedProducts.isFragile())
                .productPrice(0)
                .deliveryPrice(0)
                .totalPrice(0)
                .build();

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest request) {
        Order receivedOrder = orderRepository.findById(UUID.fromString(request.orderId())).orElseThrow(
                () -> new NoOrderFoundException("Order with id " + request.orderId() + " not found"));
        warehouseClient.returnProducts(request.products());
        receivedOrder.setState(OrderState.PRODUCT_RETURNED);

        return orderMapper.toDto(
                orderRepository.save(receivedOrder));
    }

    @Override
    public OrderDto paymentOrder(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NoOrderFoundException("Order not found: " + orderId));

        OrderDto orderDto = orderMapper.toDto(order);
        PaymentDto payment = paymentClient.create(orderDto);

        order.setPaymentId(UUID.fromString(payment.getPaymentId()));
        order.setState(OrderState.ON_PAYMENT);

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto paymentOrderFailed(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NoOrderFoundException("Order not found: " + orderId));

        order.setState(OrderState.PAYMENT_FAILED);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto deliveryOrder(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NoOrderFoundException("Order not found: " + orderId));

        order.setState(OrderState.DELIVERED);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto deliveryOrderFailed(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NoOrderFoundException("Order not found: " + orderId));

        order.setState(OrderState.DELIVERY_FAILED);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto completeOrder(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NoOrderFoundException("Order not found: " + orderId));

        order.setState(OrderState.COMPLETED);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto calculateOrderCost(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NoOrderFoundException("Order not found: " + orderId));

        OrderDto orderDto = orderMapper.toDto(order);
        double totalCost = paymentClient.calculateTotalCost(orderDto);

        order.setTotalPrice(totalCost);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto calculateDeliveryCost(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NoOrderFoundException("Order not found: " + orderId));

        OrderDto orderDto = orderMapper.toDto(order);
        double deliveryCost = deliveryClient.calculateDeliveryCost(orderDto);

        order.setDeliveryPrice(deliveryCost);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto assemblyOrder(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NoOrderFoundException("Order not found: " + orderId));
        Map<String, Long> products = order.getProducts().entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue));
        AssemblyProductsForOrderRequest request = new AssemblyProductsForOrderRequest(orderId, products);
        warehouseClient.assemblyProductForOrderFromShoppingCart(request);
        order.setState(OrderState.ASSEMBLED);

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto assemblyOrderFailed(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NoOrderFoundException("Order not found: " + orderId));

        order.setState(OrderState.ASSEMBLY_FAILED);
        return orderMapper.toDto(orderRepository.save(order));
    }
}
