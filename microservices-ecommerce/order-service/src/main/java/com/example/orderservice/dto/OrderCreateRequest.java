package com.example.orderservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderCreateRequest(
        @NotEmpty(message = "Order items cannot be empty")
        @Valid
        List<OrderItemRequest> items
) {}
