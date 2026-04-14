package com.startrust.jpa;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Product}
 */
@Data
@NoArgsConstructor
public class ProductDto implements Serializable {
    Long id;
    String name;
    Double price;
    LocalDateTime createdAt;

    public ProductDto(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}