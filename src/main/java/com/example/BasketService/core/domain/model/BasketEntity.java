package com.example.BasketService.core.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Entity
@Getter
public class BasketEntity {

    @Id
    @Column(unique = true, nullable = false)
    private String username;

    @JoinTable
    @ManyToMany
    private List<Product> products;

}
