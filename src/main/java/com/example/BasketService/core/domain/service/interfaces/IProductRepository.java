package com.example.BasketService.core.domain.service.interfaces;

import com.example.BasketService.core.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IProductRepository extends JpaRepository<Product, UUID> {
}
