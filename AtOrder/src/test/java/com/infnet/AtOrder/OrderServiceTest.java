package com.infnet.AtOrder;

import com.infnet.AtOrder.service.OrderService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderService Tests")
class OrderServiceTest {

    private MockWebServer mockProductServer;
    private MockWebServer mockPurchaseServer;
    private OrderService orderService;

    @BeforeEach
    void setUp() throws IOException {
        mockProductServer = new MockWebServer();
        mockProductServer.start();

        mockPurchaseServer = new MockWebServer();
        mockPurchaseServer.start();

        String productBaseUrl = "http://localhost:" + mockProductServer.getPort();
        String purchaseBaseUrl = "http://localhost:" + mockPurchaseServer.getPort();

        WebClient productWebClient = WebClient.builder().baseUrl(productBaseUrl).build();
        WebClient purchaseWebClient = WebClient.builder().baseUrl(purchaseBaseUrl).build();

        orderService = new OrderService(productWebClient, purchaseWebClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockProductServer.shutdown();
        mockPurchaseServer.shutdown();
    }

    @Test
    @DisplayName("Should create order successfully with valid product")
    void testCreateOrderSuccess() {
        // Arrange
        Long productId = 1L;
        int quantity = 2;
        String productJson = "{\"id\":1,\"name\":\"Teclado Mecânico\",\"price\":249.90,\"available\":true}";
        String purchaseJson = "{\"purchaseId\":100,\"status\":\"CONFIRMED\"}";

        mockProductServer.enqueue(new MockResponse()
                .setBody(productJson)
                .addHeader("Content-Type", "application/json"));

        mockPurchaseServer.enqueue(new MockResponse()
                .setBody(purchaseJson)
                .addHeader("Content-Type", "application/json"));

        StepVerifier.create(orderService.createOrder(productId, quantity))
                .assertNext(order -> {
                    assertEquals(1L, order.getProductId());
                    assertEquals("Teclado Mecânico", order.getProductName());
                    assertEquals(249.90 * quantity, order.getPrice());
                    assertEquals("CONFIRMED", order.getStatus());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should create order with different product details")
    void testCreateOrderWithDifferentProduct() {
        Long productId = 2L;
        int quantity = 1;
        String productJson = "{\"id\":2,\"name\":\"Mouse Gamer\",\"price\":150.00,\"available\":true}";
        String purchaseJson = "{\"purchaseId\":101,\"status\":\"CONFIRMED\"}";

        mockProductServer.enqueue(new MockResponse()
                .setBody(productJson)
                .addHeader("Content-Type", "application/json"));

        mockPurchaseServer.enqueue(new MockResponse()
                .setBody(purchaseJson)
                .addHeader("Content-Type", "application/json"));

        StepVerifier.create(orderService.createOrder(productId, quantity))
                .assertNext(order -> {
                    assertEquals(2L, order.getProductId());
                    assertEquals("Mouse Gamer", order.getProductName());
                    assertEquals(150.00, order.getPrice());
                    assertEquals("CONFIRMED", order.getStatus());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should calculate total price with quantity")
    void testCreateOrderCalculatesTotalPrice() {

        Long productId = 3L;
        int quantity = 5;
        String productJson = "{\"id\":3,\"name\":\"USB Cable\",\"price\":50.00,\"available\":true}";
        String purchaseJson = "{\"purchaseId\":102,\"status\":\"CONFIRMED\"}";

        mockProductServer.enqueue(new MockResponse()
                .setBody(productJson)
                .addHeader("Content-Type", "application/json"));

        mockPurchaseServer.enqueue(new MockResponse()
                .setBody(purchaseJson)
                .addHeader("Content-Type", "application/json"));

        StepVerifier.create(orderService.createOrder(productId, quantity))
                .assertNext(order -> {
                    assertEquals(250.0, order.getPrice()); // 50.00 * 5
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should handle product not found")
    void testCreateOrderProductNotFound() {

        Long productId = 999L;
        int quantity = 1;

        mockProductServer.enqueue(new MockResponse().setResponseCode(404));

        StepVerifier.create(orderService.createOrder(productId, quantity))
                .expectError()
                .verify();
    }

    @Test
    @DisplayName("Should handle purchase service error")
    void testCreateOrderPurchaseServiceError() {

        Long productId = 4L;
        int quantity = 1;
        String productJson = "{\"id\":4,\"name\":\"Monitor\",\"price\":899.99,\"available\":true}";

        mockProductServer.enqueue(new MockResponse()
                .setBody(productJson)
                .addHeader("Content-Type", "application/json"));

        mockPurchaseServer.enqueue(new MockResponse().setResponseCode(500));

        StepVerifier.create(orderService.createOrder(productId, quantity))
                .expectError()
                .verify();
    }

    @Test
    @DisplayName("Should handle zero quantity")
    void testCreateOrderWithZeroQuantity() {

        Long productId = 5L;
        int quantity = 0;
        String productJson = "{\"id\":5,\"name\":\"Headphones\",\"price\":199.99,\"available\":true}";
        String purchaseJson = "{\"purchaseId\":103,\"status\":\"CONFIRMED\"}";

        mockProductServer.enqueue(new MockResponse()
                .setBody(productJson)
                .addHeader("Content-Type", "application/json"));

        mockPurchaseServer.enqueue(new MockResponse()
                .setBody(purchaseJson)
                .addHeader("Content-Type", "application/json"));

        StepVerifier.create(orderService.createOrder(productId, quantity))
                .assertNext(order -> {
                    assertEquals(0.0, order.getPrice()); // 199.99 * 0
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should handle large quantity")
    void testCreateOrderWithLargeQuantity() {

        Long productId = 6L;
        int quantity = 100;
        String productJson = "{\"id\":6,\"name\":\"Keyboard\",\"price\":75.50,\"available\":true}";
        String purchaseJson = "{\"purchaseId\":104,\"status\":\"CONFIRMED\"}";

        mockProductServer.enqueue(new MockResponse()
                .setBody(productJson)
                .addHeader("Content-Type", "application/json"));

        mockPurchaseServer.enqueue(new MockResponse()
                .setBody(purchaseJson)
                .addHeader("Content-Type", "application/json"));

        StepVerifier.create(orderService.createOrder(productId, quantity))
                .assertNext(order -> {
                    assertEquals(7550.0, order.getPrice()); // 75.50 * 100
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should set status as CONFIRMED")
    void testCreateOrderStatusConfirmed() {

        Long productId = 7L;
        int quantity = 1;
        String productJson = "{\"id\":7,\"name\":\"Product\",\"price\":100.0,\"available\":true}";
        String purchaseJson = "{\"purchaseId\":105,\"status\":\"CONFIRMED\"}";

        mockProductServer.enqueue(new MockResponse()
                .setBody(productJson)
                .addHeader("Content-Type", "application/json"));

        mockPurchaseServer.enqueue(new MockResponse()
                .setBody(purchaseJson)
                .addHeader("Content-Type", "application/json"));

        StepVerifier.create(orderService.createOrder(productId, quantity))
                .assertNext(order -> {
                    assertEquals("CONFIRMED", order.getStatus());
                    assertNotEquals("CREATED", order.getStatus());
                    assertNotEquals("PENDING", order.getStatus());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should use purchase ID as product ID in order")
    void testCreateOrderUsesPurchaseId() {

        Long productId = 8L;
        int quantity = 1;
        Long purchaseId = 8L;
        String productJson = "{\"id\":8,\"name\":\"Item\",\"price\":50.0,\"available\":true}";
        String purchaseJson = String.format("{\"purchaseId\":%d,\"status\":\"CONFIRMED\"}", purchaseId);

        mockProductServer.enqueue(new MockResponse()
                .setBody(productJson)
                .addHeader("Content-Type", "application/json"));

        mockPurchaseServer.enqueue(new MockResponse()
                .setBody(purchaseJson)
                .addHeader("Content-Type", "application/json"));

        StepVerifier.create(orderService.createOrder(productId, quantity))
                .assertNext(order -> {
                    assertEquals(purchaseId, order.getProductId());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should correctly map product name to order")
    void testCreateOrderMapsProductName() {

        Long productId = 10L;
        int quantity = 1;
        String productName = "Premium Wireless Mouse";
        String productJson = String.format("{\"id\":10,\"name\":\"%s\",\"price\":129.90,\"available\":true}", productName);
        String purchaseJson = "{\"purchaseId\":107,\"status\":\"CONFIRMED\"}";

        mockProductServer.enqueue(new MockResponse()
                .setBody(productJson)
                .addHeader("Content-Type", "application/json"));

        mockPurchaseServer.enqueue(new MockResponse()
                .setBody(purchaseJson)
                .addHeader("Content-Type", "application/json"));

        StepVerifier.create(orderService.createOrder(productId, quantity))
                .assertNext(order -> {
                    assertEquals(productName, order.getProductName());
                })
                .verifyComplete();
    }
}
