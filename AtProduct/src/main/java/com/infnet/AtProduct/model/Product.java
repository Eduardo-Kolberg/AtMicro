package com.infnet.AtProduct.model;

public class Product {

    private Long id;
    private String name;
    private Double price;
    private boolean available;

    public Product(Long id, String name, Double price, boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.available = available;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Double getPrice() { return price; }
    public boolean isAvailable() { return available; }
}
