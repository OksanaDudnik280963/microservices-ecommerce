package com.example.productservice.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductCreateRequest(
        @NotBlank(message = "Название товара не может быть пустым")
        @Size(max = 100, message = "Название не должно превышать 100 символов")
        String name,

        String description,

        @NotNull(message = "Цена должна быть указана")
        @Positive(message = "Цена должна быть больше 0")
        BigDecimal price,

        @NotNull(message = "Количество не может быть пустым")
        @Min(value = 0, message = "Количество не может быть отрицательным")
        Integer stockQuantity
) {}
