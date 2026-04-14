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
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            createProduct("Item " + i, Math.random() * 100);
        }
        long end = System.currentTimeMillis();
        System.out.println("逐筆插入" + count + "筆耗時: " + (end - start) + "ms");
    }

    @Transactional
    public void deleteProduct(List<Long> ids) {
        productRepository.deleteAllById(ids);
    }

    // Batch Insert 測試
    @Transactional
    public void testBatchInsert(int count) {
        List<Product> producLlist = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            producLlist.add(new Product("Item " + i, Math.random() * 100));
        }

        long start = System.currentTimeMillis();
        productRepository.saveAll((Iterable<? extends Product>) producLlist);
        long end = System.currentTimeMillis();

        System.out.println("批次插入 " + count + " 筆耗時: " + (end - start) + "ms");
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
