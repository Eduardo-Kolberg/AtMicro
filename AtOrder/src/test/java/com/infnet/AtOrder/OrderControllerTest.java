package com.infnet.AtOrder;

import com.infnet.AtOrder.controller.OrderController;
import com.infnet.AtOrder.model.Order;
import com.infnet.AtOrder.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderController Tests")
class OrderControllerTest {

    @Mock
    private OrderService service;

    @Test
    @DisplayName("Should create order successfully with default quantity")
    void testCreateOrder() {
        WebTestClient client = WebTestClient.bindToController(new OrderController(service)).build();

        Order order = new Order(100L, "Teclado Mecânico", 249.90, "CONFIRMED");
        when(service.createOrder(1L, 1)).thenReturn(Mono.just(order));

        client.get().uri("/order/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.productName").isEqualTo("Teclado Mecânico")
                .jsonPath("$.price").isEqualTo(249.90)
                .jsonPath("$.status").isEqualTo("CONFIRMED");
    }

    @Test
    @DisplayName("Should create order with specified quantity")
    void testCreateOrderWithQuantity() {
        WebTestClient client = WebTestClient.bindToController(new OrderController(service)).build();

        Order order = new Order(101L, "Mouse Gamer", 300.00, "CONFIRMED");
        when(service.createOrder(2L, 2)).thenReturn(Mono.just(order));

        client.get().uri("/order/2?quantity=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.productName").isEqualTo("Mouse Gamer")
                .jsonPath("$.price").isEqualTo(300.00)
                .jsonPath("$.status").isEqualTo("CONFIRMED");
    }

    @Test
    @DisplayName("Should create order with large quantity")
    void testCreateOrderWithLargeQuantity() {
        WebTestClient client = WebTestClient.bindToController(new OrderController(service)).build();

        Order order = new Order(102L, "USB Cable", 500.00, "CONFIRMED");
        when(service.createOrder(3L, 10)).thenReturn(Mono.just(order));

        client.get().uri("/order/3?quantity=10")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.productName").isEqualTo("USB Cable")
                .jsonPath("$.price").isEqualTo(500.00);
    }

    @Test
    @DisplayName("Should return order with correct product ID")
    void testCreateOrderProductId() {
        WebTestClient client = WebTestClient.bindToController(new OrderController(service)).build();

        Order order = new Order(103L, "Monitor", 899.99, "CONFIRMED");
        when(service.createOrder(4L, 1)).thenReturn(Mono.just(order));

        client.get().uri("/order/4")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.productId").isEqualTo(103);
    }

    @Test
    @DisplayName("Should return order with confirmed status")
    void testCreateOrderStatus() {
        WebTestClient client = WebTestClient.bindToController(new OrderController(service)).build();

        Order order = new Order(104L, "Headphones", 199.99, "CONFIRMED");
        when(service.createOrder(5L, 1)).thenReturn(Mono.just(order));

        client.get().uri("/order/5")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("CONFIRMED");
    }

    @Test
    @DisplayName("Should handle service error gracefully")
    void testCreateOrderServiceError() {
        WebTestClient client = WebTestClient.bindToController(new OrderController(service)).build();

        when(service.createOrder(999L, 1))
                .thenReturn(Mono.error(new RuntimeException("Service error")));

        client.get().uri("/order/999")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("Should accept quantity as query parameter")
    void testCreateOrderQuantityParameter() {
        WebTestClient client = WebTestClient.bindToController(new OrderController(service)).build();

        Order order = new Order(105L, "Keyboard", 150.00, "CONFIRMED");
        when(service.createOrder(6L, 5)).thenReturn(Mono.just(order));

        client.get().uri("/order/6?quantity=5")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price").isEqualTo(150.00);
    }

    @Test
    @DisplayName("Should use default quantity of 1 when not specified")
    void testCreateOrderDefaultQuantity() {
        WebTestClient client = WebTestClient.bindToController(new OrderController(service)).build();

        Order order = new Order(106L, "Product", 100.0, "CONFIRMED");
        when(service.createOrder(7L, 1)).thenReturn(Mono.just(order));

        client.get().uri("/order/7")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price").isEqualTo(100.0);
    }
}
