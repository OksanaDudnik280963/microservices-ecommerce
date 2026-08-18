package com.example.orderservice.event;

import java.math.BigDecimal;

public record OrderCreatedEvent(
        String orderNumber,
        BigDecimal totalPrice,
        String customerEmail
) {}
