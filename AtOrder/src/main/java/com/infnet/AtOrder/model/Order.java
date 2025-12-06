package com.infnet.AtOrder.model;

public class Order {
    private Long productId;
    private String productName;
    private Double price;
    private String status;

    public Order(Long productId, String productName, Double price, String status) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.status = status;
    }

    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public Double getPrice() { return price; }
    public String getStatus() { return status; }
}
