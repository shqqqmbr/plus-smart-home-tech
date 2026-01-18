package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.client.DeliveryClient;
import ru.yandex.practicum.client.PaymentClient;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.constant.DeliveryState;
import ru.yandex.practicum.constant.OrderState;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NoOrderFoundException;
import ru.yandex.practicum.mapper.AddressMapper;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.repository.OrderRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AddressMapper addressMapper;
    private final WarehouseClient warehouseClient;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;

    @Override
    public PageResponse<OrderDto> getOrders(String username, Integer page, Integer size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        return getAllOrdersInternal(username, pageable);
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest request) {
        ShoppingCartDto cart = request.shoppingCart();
        BookedProductsDto bookedProducts = warehouseClient.checkQuantity(cart);

        Map<UUID, Integer> productsMap = cart.getProducts().entrySet().stream()
                .collect(Collectors.toMap(
                        e -> UUID.fromString(e.getKey()),
                        Map.Entry::getValue
                ));

        Order order = Order.builder()
                .shoppingCartId(UUID.fromString(cart.getShoppingCartId()))
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

        ShoppingCartDto cart = ShoppingCartDto.builder()
                .shoppingCartId(order.getShoppingCartId().toString())
                .products(order.getProducts().entrySet().stream()
                        .collect(Collectors.toMap(
                                e -> e.getKey().toString(),
                                Map.Entry::getValue
                        )))
                .build();

        warehouseClient.assemblyProductForOrderFromShoppingCart(cart);
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

    private PageResponse<OrderDto> getAllOrdersInternal(String username,
                                                        Pageable pageable) {
        Page<Order> productPage = orderRepository.findAllByUsername(username, pageable);
        Page<OrderDto> dtoPage = productPage.map(orderMapper::toDto);
        return PageResponse.fromPage(dtoPage);
    }

    private Pageable createPageable(int page, int size, String sort) {
        if (sort != null && !sort.isBlank()) {
            String[] sortParams = sort.split(",");
            if (sortParams.length == 2) {
                Sort.Direction direction = sortParams[1].trim().equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;
                return PageRequest.of(page, size, Sort.by(direction, sortParams[0].trim()));
            }
            return PageRequest.of(page, size, Sort.by(sortParams[0].trim()));
        }
        return PageRequest.of(page, size);
    }
}
