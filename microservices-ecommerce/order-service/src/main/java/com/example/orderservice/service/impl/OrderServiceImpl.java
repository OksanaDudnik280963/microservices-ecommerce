package com.example.orderservice.service.impl;

import com.example.orderservice.client.ProductClient;
import com.example.orderservice.client.dto.ProductResponseDto;
import com.example.orderservice.dto.*;
import com.example.orderservice.entity.*;
import com.example.orderservice.event.OrderCreatedEvent;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    @Override
    @Transactional
    @CircuitBreaker(name = "productServiceCB", fallbackMethod = "createOrderFallback")
    public OrderResponse createOrder(OrderCreateRequest request) {
        log.info("Оформление нового заказа...");

        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .status(OrderStatus.CREATED)
                .build();

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.items()) {
            ProductResponseDto product = this.productClient.getProductById(itemRequest.productId());

            if (product.stockQuantity() < itemRequest.quantity()) {
                throw new IllegalStateException("Недостаточно товара на складе: " + product.name());
            }

            BigDecimal itemTotal = product.price().multiply(BigDecimal.valueOf(itemRequest.quantity()));
            totalPrice = totalPrice.add(itemTotal);

            OrderItem orderItem = OrderItem.builder()
                    .productId(product.id())
                    .quantity(itemRequest.quantity())
                    .price(product.price())
                    .build();

            order.addItem(orderItem);
        }

        order.setTotalPrice(totalPrice);
        Order savedOrder = this.orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getOrderNumber(),
                savedOrder.getTotalPrice(),
                "customer@example.com"
        );
        this.kafkaTemplate.send("notification-topic", event);
        log.info("Событие OrderCreatedEvent отправлено в Kafka для заказа {}", savedOrder.getOrderNumber());

        return mapToResponse(savedOrder);
    }

    public OrderResponse createOrderFallback(OrderCreateRequest request, Throwable throwable) {
        log.error("❌ Fallback сработал! product-service недоступен или превышен таймаут. Причина: {}", 
                throwable.getMessage());

        throw new RuntimeException("Сервис товаров временно недоступен. Попробуйте оформить заказ позже.");
    }

    private OrderResponse mapToResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                order.getItems().stream()
                        .map(i -> new OrderItemDto(i.getProductId(), i.getQuantity(), i.getPrice()))
                        .toList()
        );
    }
}
