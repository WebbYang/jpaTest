package com.startrust.jpa;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    // Batch Insert 測試
    @Transactional
    public void testBatchInsert(int count) {
        List<Product> producList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            producList.add(new Product("Item " + i, Math.random() * 100));
        }
        productRepository.saveAll(producList);
    }

    @Transactional
    public void deleteProduct(List<Long> ids) {
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

    @Transactional
    public void testBatchUpdate(int i) {
        List<Long> ids = new ArrayList<>();
        for (int j = 0; j < i; j++) {
            ids.add((long) j + 1);
        }
        List<Product> products = productRepository.findAllById(ids);
        for (Product p : products) {
            p.setPrice(p.getPrice() * 1.1); // 將價格增加 10%
        }
        productRepository.saveAll(products);
    }

    @Transactional
    public void testUpdateOneByOne(int count) {
        for (int i = 0; i < count; i++) {
            Product product = productRepository.findById((long) i + 1).orElseThrow();
            product.setPrice(product.getPrice() * 1.1); // 將價格增加 10%
            productRepository.save(product);
        }
    }
}
