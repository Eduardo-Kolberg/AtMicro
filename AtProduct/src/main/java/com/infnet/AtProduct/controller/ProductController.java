package com.infnet.AtProduct.controller;

import com.infnet.AtProduct.model.Product;
import com.infnet.AtProduct.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Product product = service.getProductById(id);
        return product != null ?
                ResponseEntity.ok(product) :
                ResponseEntity.notFound().build();
    }
}