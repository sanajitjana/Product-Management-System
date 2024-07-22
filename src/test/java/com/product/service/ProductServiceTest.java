package com.product.service;

import com.product.entity.Product;
import com.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productServiceimpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddProduct() {

        Product product = new Product();
        product.setName("Iphone");

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productServiceimpl.addProduct(product);

        assertEquals("Iphone", savedProduct.getName());

    }

    @Test
    public void testGetProducts() {

        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Ipad");
        product1.setCategory("electronics");
        product1.setPrice(79999.99);
        product1.setInStock(true);
        product1.setRating(4.3);
        product1.setCreatedAt(LocalDateTime.now());

        when(productRepository.findByCategory("electronics")).thenReturn(Arrays.asList(product1));

        List<Product> products = productServiceimpl.getProducts(Optional.of("electronics"), Optional.empty(), Optional.empty(), Optional.empty(), "price", "asc");

        assertEquals(1, products.size());
        assertEquals("Ipad", products.get(0).getName());

    }

}
