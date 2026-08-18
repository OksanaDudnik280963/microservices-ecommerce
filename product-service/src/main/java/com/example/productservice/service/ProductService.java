package com.example.productservice.service;

import com.example.productservice.dto.ProductCreateRequest;
import com.example.productservice.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductCreateRequest request);
    ProductResponse getProductById(Long id);
    List<ProductResponse> getAllProducts();
}
