package com.startrust.jpa;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void createProduct(String name, Double price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        productRepository.save(product);
    }

    @Transactional
    public void testInsertOneByOne(int count) {
        for (int i = 0; i < count; i++) {
            createProduct("Item " + i, Math.random() * 100);
        }
    }

    @Transactional
    public void testBatchInsert(int count) {
        List<Product> productList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            productList.add(new Product("Item " + i, Math.random() * 100));
        }
        productRepository.saveAll(productList);

    }

    @Transactional
    public void deleteProduct(List<UUID> ids) {
        productRepository.deleteAllById(ids);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public List<ProductDto> getAllProducts() {
        return findAllProducts().stream()
                .map(p -> new ProductDto(p.getId(), p.getName(), p.getPrice()))
                .toList();
    }
}
