package com.infnet.AtOrder.controller;

import com.infnet.AtOrder.model.Order;
import com.infnet.AtOrder.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping("/{productId}")
    public Mono<Order> createOrder(@PathVariable Long productId, @RequestParam(defaultValue = "1") int quantity) {
        return service.createOrder(productId, quantity);
    }
}
