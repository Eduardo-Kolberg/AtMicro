package com.infnet.AtPurchase.controller;

import com.infnet.AtPurchase.model.Purchase;
import com.infnet.AtPurchase.service.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PurchaseControllerTest {

    @Mock
    private PurchaseService service;

    @InjectMocks
    private PurchaseController controller;

    private Purchase purchase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        purchase = new Purchase(1L, "Product Test", 99.99, "PENDING");
        purchase.setId(1L);
    }

    @Test
    void testCreatePurchaseSuccess() {
        // Arrange
        Purchase inputPurchase = new Purchase(1L, "Product Test", 99.99, "PENDING");
        when(service.createPurchase(any(Purchase.class))).thenReturn(purchase);

        // Act
        ResponseEntity<Purchase> response = controller.createPurchase(inputPurchase);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(purchase, response.getBody());
        assertEquals("CONFIRMED", inputPurchase.getStatus());
        verify(service, times(1)).createPurchase(any(Purchase.class));
    }

    @Test
    void testGetAllPurchasesSuccess() {
        // Arrange
        Purchase purchase2 = new Purchase(2L, "Product Test 2", 150.00, "CONFIRMED");
        purchase2.setId(2L);
        List<Purchase> purchases = Arrays.asList(purchase, purchase2);
        when(service.getAllPurchases()).thenReturn(purchases);

        // Act
        ResponseEntity<List<Purchase>> response = controller.getAllPurchases();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(purchases, response.getBody());
        verify(service, times(1)).getAllPurchases();
    }

    @Test
    void testGetAllPurchasesEmpty() {
        // Arrange
        when(service.getAllPurchases()).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<List<Purchase>> response = controller.getAllPurchases();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(service, times(1)).getAllPurchases();
    }

    @Test
    void testCreatePurchaseSetsStatus() {
        // Arrange
        Purchase inputPurchase = new Purchase(1L, "Product Test", 99.99, "PENDING");
        when(service.createPurchase(any(Purchase.class))).thenReturn(purchase);

        // Act
        controller.createPurchase(inputPurchase);

        // Assert
        assertEquals("CONFIRMED", inputPurchase.getStatus());
    }
}

