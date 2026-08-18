package com.example.orderservice.service;

import com.example.orderservice.dto.OrderCreateRequest;
import com.example.orderservice.dto.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(OrderCreateRequest request);
}
