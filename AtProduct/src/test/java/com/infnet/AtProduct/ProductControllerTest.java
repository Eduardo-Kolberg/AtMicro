package com.infnet.AtProduct;


import com.infnet.AtProduct.model.Product;
import com.infnet.AtProduct.service.ProductService;
import com.infnet.AtProduct.controller.ProductController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Test
    void shouldReturnProductWhenExists() throws Exception {
        when(service.getProductById(1L))
                .thenReturn(new Product(1L, "Teclado Mecânico", 249.90, true));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Teclado Mecânico"));
    }

    @Test
    void shouldReturnNotFoundForInvalidId() throws Exception {
        when(service.getProductById(99L)).thenReturn(null);

        mockMvc.perform(get("/products/99"))
                .andExpect(status().isNotFound());
    }
}
