package com.example.BasketService.core.domain.service.interfaces;

import com.example.BasketService.core.domain.model.Basket;
import com.example.BasketService.core.domain.model.BasketComponent;
import com.example.BasketService.core.domain.model.BasketEntity;
import com.example.BasketService.core.domain.model.Product;

import java.util.UUID;

public interface IBasketService {

    Product createProduct(Product product);

    Product updateProduct(Product product);

    String deleteProduct(UUID id);

    void createEmptyBasketForUser(UUID userId);

    BasketComponent addToBasket(BasketComponent basketComponent);

    String deleteFromBasket(BasketComponent basketComponent);

    Basket getBasketFromUser(UUID username);

    Basket fromEntity(BasketEntity entity);

    BasketEntity toEntity(Basket basket);
}
