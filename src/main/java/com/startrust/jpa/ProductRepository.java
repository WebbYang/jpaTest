package com.startrust.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT MAX(p.id) FROM Product p")
    Optional<Long> findLastId();
}