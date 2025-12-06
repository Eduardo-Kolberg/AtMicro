package com.infnet.AtPurchase.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseTest {

    private Purchase purchase;

    @BeforeEach
    void setUp() {
        purchase = new Purchase();
    }

    @Test
    void testPurchaseConstructorEmpty() {
        // Assert
        assertNull(purchase.getId());
        assertNull(purchase.getProductId());
        assertNull(purchase.getProductName());
        assertNull(purchase.getPrice());
        assertNull(purchase.getStatus());
    }

    @Test
    void testPurchaseConstructorWithParameters() {
        // Act
        Purchase newPurchase = new Purchase(1L, "Test Product", 99.99, "CONFIRMED");

        // Assert
        assertEquals(1L, newPurchase.getProductId());
        assertEquals("Test Product", newPurchase.getProductName());
        assertEquals(99.99, newPurchase.getPrice());
        assertEquals("CONFIRMED", newPurchase.getStatus());
    }

    @Test
    void testSetAndGetId() {
        // Act
        purchase.setId(10L);

        // Assert
        assertEquals(10L, purchase.getId());
    }

    @Test
    void testSetAndGetProductId() {
        // Act
        purchase.setProductId(5L);

        // Assert
        assertEquals(5L, purchase.getProductId());
    }

    @Test
    void testSetAndGetProductName() {
        // Act
        purchase.setProductName("Laptop");

        // Assert
        assertEquals("Laptop", purchase.getProductName());
    }

    @Test
    void testSetAndGetPrice() {
        // Act
        purchase.setPrice(1500.50);

        // Assert
        assertEquals(1500.50, purchase.getPrice());
    }

    @Test
    void testSetAndGetStatus() {
        // Act
        purchase.setStatus("PENDING");

        // Assert
        assertEquals("PENDING", purchase.getStatus());
    }

    @Test
    void testPurchaseCompleteFlow() {
        // Act
        purchase.setId(1L);
        purchase.setProductId(100L);
        purchase.setProductName("Monitor");
        purchase.setPrice(500.00);
        purchase.setStatus("CONFIRMED");

        // Assert
        assertEquals(1L, purchase.getId());
        assertEquals(100L, purchase.getProductId());
        assertEquals("Monitor", purchase.getProductName());
        assertEquals(500.00, purchase.getPrice());
        assertEquals("CONFIRMED", purchase.getStatus());
    }

    @Test
    void testPurchaseWithNullValues() {
        // Act
        purchase.setProductName(null);
        purchase.setStatus(null);

        // Assert
        assertNull(purchase.getProductName());
        assertNull(purchase.getStatus());
    }

    @Test
    void testPurchaseWithDifferentPrices() {
        // Arrange & Act
        purchase.setPrice(0.0);
        double zeroPrice = purchase.getPrice();

        purchase.setPrice(1000000.99);
        double largePrice = purchase.getPrice();

        // Assert
        assertEquals(0.0, zeroPrice);
        assertEquals(1000000.99, largePrice);
    }
}

