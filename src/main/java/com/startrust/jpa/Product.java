package com.startrust.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "product")
public class Product implements Persistable<Long> {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Transient
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    @PostLoad
    private void markNotNew() {
        this.isNew = false;
    }

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Product(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}