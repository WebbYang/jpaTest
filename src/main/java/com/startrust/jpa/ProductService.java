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

//    @Transactional
//    public void createProduct(String name, Double price) {
//        Long lastId = productRepository.findLastId().orElse(0L);
//        Product product = new Product();
//        product.setId(lastId + 1);
//        product.setName(name);
//        product.setPrice(price);
//        productRepository.save(product);
//    }

//    @Transactional
    public void createProduct(Long id, String name, Double price) {
        Product product = new Product();
        product.setId(id); // 直接使用傳入的 ID
        product.setName(name);
        product.setPrice(price);
        productRepository.save(product);
    }

//    @Transactional
    public void testInsertOneByOne(int count) {
        long lastId = productRepository.findLastId().orElse(0L);
        for (int i = 0; i < count; i++) {
            Long newId = lastId + i + 1;
            createProduct(newId,"Item " + i, Math.random() * 100);
        }
    }

    @Transactional
    public void deleteProduct(List<Long> ids) {
        productRepository.deleteAllById(ids);
    }

    // Batch Insert 測試
//    @Transactional
    public void testBatchInsert(int count) {
        List<Product> producLlist = new ArrayList<>();
        long lastId = productRepository.findLastId().orElse(0L);
        for (int i = 0; i < count; i++) {
            Long newId = lastId + i + 1; // 確保 ID 是唯一且遞增的
            producLlist.add(new Product(newId, "Item " + i, Math.random() * 100));
        }
        productRepository.saveAll(producLlist);
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
