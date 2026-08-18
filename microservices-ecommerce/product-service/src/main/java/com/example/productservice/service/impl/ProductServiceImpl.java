package com.example.productservice.service.impl;

import com.example.productservice.dto.ProductCreateRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.entity.Product;
import com.example.productservice.exception.ProductNotFoundException;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    @Override
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        log.info("Создание нового товара с названием: {}", request.name());

        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .stockQuantity(request.stockQuantity())
                .build();

        Product savedProduct = this.productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        log.info("Поиск товара по ID: {}", id);

        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар с ID " + id + " не найден"));

        return mapToResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        log.info("Запрос всех товаров");

        return this.productRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
