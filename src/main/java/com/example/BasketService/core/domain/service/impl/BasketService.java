package com.example.BasketService.core.domain.service.impl;

import com.example.BasketService.core.domain.model.Basket;
import com.example.BasketService.core.domain.model.BasketEntity;
import com.example.BasketService.core.domain.model.Product;

public class BasketService {

    public Basket fromEntity(BasketEntity entity){
        int price = 0;
        for (Product p: entity.getProducts()) {
            price += p.getPrice();
        };
        return new Basket(entity.getUsername(), entity.getProducts(), price);
    }
}
