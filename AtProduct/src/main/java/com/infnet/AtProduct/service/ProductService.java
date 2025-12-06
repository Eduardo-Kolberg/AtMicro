package com.infnet.AtProduct.service;

import com.infnet.AtProduct.model.Product;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {
    private final List<Product> products = Arrays.asList(
            new Product(1L, "Teclado Mecânico", 249.90, true),
            new Product(2L, "Mouse Gamer", 149.90, true),
            new Product(3L, "Monitor 27''", 1299.00, false)
    );

    public Product getProductById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
