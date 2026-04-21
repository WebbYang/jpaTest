package com.startrust.jpa;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final org.hibernate.stat.Statistics statistics;

    public ProductController(ProductService productService, EntityManagerFactory emf) {
        this.productService = productService;
        this.statistics = emf.unwrap(org.hibernate.SessionFactory.class).getStatistics();
        this.statistics.setStatisticsEnabled(true);
    }

    @PostMapping("/batch-insert-test")
    public ResponseEntity<String> batchInsertTest(@RequestParam int count) {
        return runBenchmark("batch", count, productService::testBatchInsert);
    }

    @PostMapping("/insert-individual-test")
    public ResponseEntity<String> insertIndividualTest(@RequestParam int count) {
        return runBenchmark("individual", count, productService::testInsertOneByOne);
    }

    private ResponseEntity<String> runBenchmark(String mode, int count, InsertOperation operation) {
        statistics.clear();
        long start = System.currentTimeMillis();
        operation.run(count);
        long elapsed = System.currentTimeMillis() - start;

        String summary = buildSummary(mode, count, elapsed);
        System.out.println(summary);
        return ResponseEntity.ok(summary);
    }

    private String buildSummary(String mode, int count, long elapsedMs) {
        long entityInsertCount = statistics.getEntityInsertCount();
        long prepareStatementCount = statistics.getPrepareStatementCount();
        long transactionCount = statistics.getTransactionCount();

        return "mode=" + mode +
                ", requestedRows=" + count +
                ", elapsedMs=" + elapsedMs +
                ", entityInsertCount=" + entityInsertCount +
                ", prepareStatementCount=" + prepareStatementCount +
                ", transactionCount=" + transactionCount +
                ", batchSignal=" + (prepareStatementCount < entityInsertCount ? "possible" : "not obvious");
    }

    @FunctionalInterface
    private interface InsertOperation {
        void run(int count);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @DeleteMapping
    public ResponseEntity<String> deleteProducts(@RequestBody List<UUID> ids) {
        productService.deleteProduct(ids);
        return ResponseEntity.ok("Products deleted successfully");
    }
}
