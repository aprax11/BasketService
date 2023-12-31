package com.example.BasketService.core.domain.service.interfaces;

import com.example.BasketService.core.domain.model.BasketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface IBasketRepository extends JpaRepository<BasketEntity, UUID> {


}
