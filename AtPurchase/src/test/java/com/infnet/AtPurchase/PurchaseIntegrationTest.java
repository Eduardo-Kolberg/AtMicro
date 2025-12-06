package com.infnet.AtPurchase;

import com.infnet.AtPurchase.model.Purchase;
import com.infnet.AtPurchase.repository.PurchaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PurchaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PurchaseRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void testCreatePurchaseIntegration() throws Exception {
        // Arrange
        Purchase purchase = new Purchase(1L, "Test Product", 99.99, "PENDING");
        String purchaseJson = objectMapper.writeValueAsString(purchase);

        // Act & Assert
        mockMvc.perform(post("/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(purchaseJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName", equalTo("Test Product")))
                .andExpect(jsonPath("$.status", equalTo("CONFIRMED")))
                .andExpect(jsonPath("$.price", equalTo(99.99)));
    }

    @Test
    void testGetAllPurchasesIntegration() throws Exception {
        // Arrange
        Purchase purchase1 = new Purchase(1L, "Product 1", 50.0, "CONFIRMED");
        Purchase purchase2 = new Purchase(2L, "Product 2", 75.0, "CONFIRMED");
        repository.save(purchase1);
        repository.save(purchase2);

        // Act & Assert
        mockMvc.perform(get("/purchase")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productName", equalTo("Product 1")))
                .andExpect(jsonPath("$[1].productName", equalTo("Product 2")));
    }

    @Test
    void testGetAllPurchasesEmptyIntegration() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/purchase")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testCreateMultiplePurchasesIntegration() throws Exception {
        // Arrange
        Purchase purchase1 = new Purchase(1L, "Product 1", 100.0, "PENDING");
        Purchase purchase2 = new Purchase(2L, "Product 2", 200.0, "PENDING");

        String purchaseJson1 = objectMapper.writeValueAsString(purchase1);
        String purchaseJson2 = objectMapper.writeValueAsString(purchase2);

        // Act
        mockMvc.perform(post("/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(purchaseJson1))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(purchaseJson2))
                .andExpect(status().isCreated());

        // Assert
        mockMvc.perform(get("/purchase"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}

