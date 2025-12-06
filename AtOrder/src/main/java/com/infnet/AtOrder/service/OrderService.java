package com.infnet.AtOrder.service;

import com.infnet.AtOrder.model.Order;
import com.infnet.AtOrder.model.Product;
import com.infnet.AtOrder.model.PurchaseRequest;
import com.infnet.AtOrder.model.PurchaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    private final WebClient webClient;
    private final WebClient purchaseWebClient;

    @Autowired
    public OrderService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://product-service:8080").build();
        this.purchaseWebClient = builder.baseUrl("http://purchase-service:8082").build();
    }

    public OrderService(WebClient webClient, WebClient purchaseWebClient) {
        this.webClient = webClient;
        this.purchaseWebClient = purchaseWebClient;
    }

    public Mono<Order> createOrder(Long productId, int quantity) {
        Mono<Product> productMono = webClient.get()
                .uri("/products/{id}", productId)
                .retrieve()
                .bodyToMono(Product.class);

        return productMono.flatMap(product -> {
            double totalPrice = product.price() * quantity;

            PurchaseRequest purchaseRequest = new PurchaseRequest(productId, product.name(), totalPrice);

            return purchaseWebClient.post()
                    .uri("/purchase")
                    .bodyValue(purchaseRequest)
                    .retrieve()
                    .bodyToMono(PurchaseResponse.class)
                    .map(purchaseResponse -> new Order(purchaseRequest.getProductId(), product.name(), totalPrice, "CONFIRMED"));
        });
    }
}
