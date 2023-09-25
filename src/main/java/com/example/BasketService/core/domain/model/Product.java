package com.example.BasketService.core.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "BasketProduct")
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @Column(unique = true, nullable = false)
    private UUID id;

    private String name;

    private String price;

    private String image;

}
