package com.example.notificationservice.event;

import java.math.BigDecimal;

public record OrderCreatedEvent(
        String orderNumber,
        BigDecimal totalPrice,
        String customerEmail
) {}
