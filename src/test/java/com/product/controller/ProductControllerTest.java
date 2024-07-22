package com.product.controller;

import com.product.entity.Product;
import com.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void testSaveProduct() throws Exception {

        Product product = new Product();
        product.setName("Iphone");

        when(productService.addProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/products")
                        .contentType("application/json")
                        .content("{\"name\":\"Iphone\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Iphone"));
    }

    @Test
    public void testGetProducts() throws Exception {

        Product product = new Product();
        product.setId(1);
        product.setName("Ipad");
        product.setCategory("electronics");
        product.setPrice(79999.99);
        product.setInStock(true);
        product.setRating(4.3);
        product.setCreatedAt(LocalDateTime.now());

        when(productService.getProducts(Optional.of("electronics"), Optional.empty(), Optional.empty(), Optional.empty(), "price", "asc"))
                .thenReturn(Arrays.asList(product));

        mockMvc.perform(get("/api/products")
                        .param("category", "electronics")
                        .param("sortField", "price")
                        .param("sortOrder", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Ipad"));

    }

}
