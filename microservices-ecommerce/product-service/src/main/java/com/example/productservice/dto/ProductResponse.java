package com.example.productservice.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        LocalDateTime createdAt
) {}
