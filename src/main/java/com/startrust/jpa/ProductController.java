package com.startrust.jpa;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final Statistics statistics;

    public ProductController(ProductService productService, EntityManagerFactory entityManagerFactory) {
        this.productService = productService;
        this.statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
        this.statistics.setStatisticsEnabled(true);
    }

    @PostMapping("/batch-insert-test")
    public ResponseEntity<String> batchInsertTest(@RequestParam int count) {
        statistics.clear();
        long start = System.currentTimeMillis();
        productService.testBatchInsert(count); // 測試批次插入 n 筆資料
        long end = System.currentTimeMillis();
        System.out.println("批次插入 " + count + " 筆耗時: " + (end - start) + "ms");
        printStatistics();
        return ResponseEntity.ok("Batch insert test executed");
    }

    @PostMapping("/insert-individual-test")
    public ResponseEntity<String> insertIndividualTest(@RequestParam int count) {
        statistics.clear();
        long start = System.currentTimeMillis();
        productService.testInsertOneByOne(count); // 測試逐筆插入 n 筆資料
        long end = System.currentTimeMillis();
        System.out.println("批次插入 " + count + " 筆耗時: " + (end - start) + "ms");
        printStatistics();
        return ResponseEntity.ok("Individual insert test executed");
    }

    private void printStatistics() {
        System.out.println("實體新增總數: " + statistics.getEntityInsertCount());
        // 代表與資料庫通訊的次數 (這是 Batch 是否成功的關鍵)
        System.out.println("JDBC 語句準備次數: " + statistics.getPrepareStatementCount());

        if (statistics.getPrepareStatementCount() < statistics.getEntityInsertCount()) {
            System.out.println("檢測到 Batch 執行成功！有效減少了網路往返次數。");
        }
        System.out.println("============================");
    }


    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @DeleteMapping
    public ResponseEntity<String> deleteProducts(@RequestBody List<Long> ids) {
        productService.deleteProduct(ids);
        return ResponseEntity.ok("Products deleted successfully");
    }

}
