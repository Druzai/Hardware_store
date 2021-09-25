package com.app.services;

import com.app.ProgramTests;
import com.app.models.Product;
import com.app.repositories.ProductRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ProductTest extends ProgramTests {
    @MockBean
    private ProductRepository productRepository;

    @Captor
    private ArgumentCaptor<Product> captor;
    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    private final List<Product> products = new ArrayList<>();

    @Before("")
    public void setUp() {
        var product = new Product("test","descr", null, "KIA",
                "latex", "Russia", "test things", 300, 12.5);

        products.add(product);
        when(productRepository.findAll()).thenReturn(products);
    }

    @Test
    public void whenGetOrders() {
        var fetched = productService.getAllProducts();
        verify(productRepository, times(1)).findAll();
        Assertions.assertEquals(products.size(), fetched.size());
    }

    @Test
    public void whenAddOrder() {
        var product2 = new Product("test2","description", null, "KIA",
                "latex", "Russia", "test things", 3000, 120.5);
        productService.addProduct(product2);
        verify(productRepository).save(captor.capture());
        var captured = captor.getValue();
        Assertions.assertEquals(product2.getName(), captured.getName());
        Assertions.assertEquals(product2.getDescription(), captured.getDescription());
    }
}
