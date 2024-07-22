package com.product.service;


import com.product.entity.Product;
import com.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public Product addProduct(Product product) {

        product.setCreatedAt(LocalDateTime.now());
        return productRepository.save(product);

    }

    @Override
    public List<Product> getProducts(Optional<String> category, Optional<Double> minPrice, Optional<Double> maxPrice, Optional<Boolean> inStock, String sortField, String sortOrder) {

        List<Product> products;

        if (category.isPresent() || minPrice.isPresent() || maxPrice.isPresent() || inStock.isPresent()) {

            if (category.isPresent()) {
                products = productRepository.findByCategory(category.get());
            } else {
                products = productRepository.findAll();
            }

            if (minPrice.isPresent() && maxPrice.isPresent()) {
                products = products.stream().filter(p -> p.getPrice() >= minPrice.get() && p.getPrice() <= maxPrice.get()).collect(Collectors.toList());
            }

            if (inStock.isPresent()) {
                products = products.stream().filter(p -> p.getInStock().equals(inStock.get())).collect(Collectors.toList());
            }

        } else {
            products = productRepository.findAll();
        }

        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
        return products.stream().sorted((p1, p2) -> {
            switch (sortField) {
                case "price":
                    return sortOrder.equals("asc") ? p1.getPrice().compareTo(p2.getPrice()) : p2.getPrice().compareTo(p1.getPrice());
                case "rating":
                    return sortOrder.equals("asc") ? p1.getRating().compareTo(p2.getRating()) : p2.getRating().compareTo(p1.getRating());
                case "createdAt":
                    return sortOrder.equals("asc") ? p1.getCreatedAt().compareTo(p2.getCreatedAt()) : p2.getCreatedAt().compareTo(p1.getCreatedAt());
                default:
                    return 0;
            }
        }).collect(Collectors.toList());

    }
}
