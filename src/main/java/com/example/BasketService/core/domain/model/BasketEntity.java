package com.example.BasketService.core.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "BasketEntity", schema = "basket")
@AllArgsConstructor
@NoArgsConstructor
public class BasketEntity {

    @Id
    @Column(unique = true, nullable = false)
    private UUID userId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(schema="basket")
    private List<Product> products;

}
