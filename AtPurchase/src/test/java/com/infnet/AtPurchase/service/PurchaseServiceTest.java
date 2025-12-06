package com.infnet.AtPurchase.service;

import com.infnet.AtPurchase.model.Purchase;
import com.infnet.AtPurchase.repository.PurchaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PurchaseServiceTest {

    @Mock
    private PurchaseRepository repository;

    @InjectMocks
    private PurchaseService service;

    private Purchase purchase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        purchase = new Purchase(1L, "Product Test", 99.99, "CONFIRMED");
        purchase.setId(1L);
    }

    @Test
    void testCreatePurchaseSuccess() {
        // Arrange
        Purchase inputPurchase = new Purchase(1L, "Product Test", 99.99, "CONFIRMED");
        when(repository.save(inputPurchase)).thenReturn(purchase);

        // Act
        Purchase result = service.createPurchase(inputPurchase);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Product Test", result.getProductName());
        assertEquals(99.99, result.getPrice());
        assertEquals("CONFIRMED", result.getStatus());
        verify(repository, times(1)).save(inputPurchase);
    }

    @Test
    void testGetAllPurchasesSuccess() {
        // Arrange
        Purchase purchase2 = new Purchase(2L, "Product Test 2", 150.00, "CONFIRMED");
        purchase2.setId(2L);
        List<Purchase> purchases = Arrays.asList(purchase, purchase2);
        when(repository.findAll()).thenReturn(purchases);

        // Act
        List<Purchase> result = service.getAllPurchases();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product Test", result.get(0).getProductName());
        assertEquals("Product Test 2", result.get(1).getProductName());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetAllPurchasesEmpty() {
        // Arrange
        when(repository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Purchase> result = service.getAllPurchases();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testCreatePurchaseWithDifferentValues() {
        // Arrange
        Purchase inputPurchase = new Purchase(5L, "Expensive Item", 999.99, "CONFIRMED");
        Purchase expectedResult = new Purchase(5L, "Expensive Item", 999.99, "CONFIRMED");
        expectedResult.setId(5L);
        when(repository.save(inputPurchase)).thenReturn(expectedResult);

        // Act
        Purchase result = service.createPurchase(inputPurchase);

        // Assert
        assertEquals(5L, result.getId());
        assertEquals(999.99, result.getPrice());
        assertEquals("Expensive Item", result.getProductName());
    }

    @Test
    void testGetAllPurchasesMultiple() {
        // Arrange
        List<Purchase> expectedPurchases = Arrays.asList(
                new Purchase(1L, "Product 1", 10.0, "CONFIRMED"),
                new Purchase(2L, "Product 2", 20.0, "CONFIRMED"),
                new Purchase(3L, "Product 3", 30.0, "CONFIRMED")
        );
        when(repository.findAll()).thenReturn(expectedPurchases);

        // Act
        List<Purchase> result = service.getAllPurchases();

        // Assert
        assertEquals(3, result.size());
        assertEquals(expectedPurchases, result);
    }
}

