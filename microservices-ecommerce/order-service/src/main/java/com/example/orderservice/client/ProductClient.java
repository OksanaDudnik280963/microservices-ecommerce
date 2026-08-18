package com.example.orderservice.client;

import com.example.orderservice.client.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductClient {
    @GetMapping("/api/v1/products/{id}")
    ProductResponseDto getProductById(@PathVariable("id") Long id);
}
