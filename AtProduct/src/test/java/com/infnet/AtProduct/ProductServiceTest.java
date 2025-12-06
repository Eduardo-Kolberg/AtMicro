package com.infnet.AtProduct;

import com.infnet.AtProduct.model.Product;
import com.infnet.AtProduct.service.ProductService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    ProductService service = new ProductService();

    @Test
    void shouldReturnProductWhenIdExists() {
        Product product = service.getProductById(1L);
        assertNotNull(product);
        assertEquals("Teclado Mecânico", product.getName());
    }

    @Test
    void shouldReturnNullWhenIdDoesNotExist() {
        Product product = service.getProductById(999L);
        assertNull(product);
    }
}
