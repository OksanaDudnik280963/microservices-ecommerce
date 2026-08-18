package com.example.orderservice.dto;

import com.example.orderservice.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        OrderStatus status,
        BigDecimal totalPrice,
        LocalDateTime createdAt,
        List<OrderItemDto> items
) {}

