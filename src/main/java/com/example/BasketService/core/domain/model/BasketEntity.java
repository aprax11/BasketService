package com.example.BasketService.core.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BasketEntity {

    @Id
    @Column(unique = true, nullable = false)
    private String username;

    @JoinTable
    @ManyToMany
    private List<Product> products;

}
