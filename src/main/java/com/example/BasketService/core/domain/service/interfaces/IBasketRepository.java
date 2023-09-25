package com.example.BasketService.core.domain.service.interfaces;

import com.example.BasketService.core.domain.model.BasketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IBasketRepository extends JpaRepository<BasketEntity, String> {
    @Query("SELECT b FROM BasketEntity b WHERE b.username = ?1")
    Optional<BasketEntity> findBasketById(String username);
}
