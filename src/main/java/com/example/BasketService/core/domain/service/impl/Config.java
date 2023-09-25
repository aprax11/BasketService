package com.example.BasketService.core.domain.service.impl;

import com.example.BasketService.core.domain.model.BasketComponent;
import com.example.BasketService.core.domain.model.Product;
import com.example.BasketService.core.domain.service.interfaces.IBasketService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
@Configuration
public class Config {
    @Bean
    CommandLineRunner commandLineRunner(IBasketService basketService){
        return args -> {
            Product product1 = new Product(
                    UUID.randomUUID(),
                    "Ring",
                    "22€",
                    "1"
            );


            basketService.createProduct(product1);
            BasketComponent basketComponent = new BasketComponent(UUID.randomUUID(), product1);
            basketService.addToBasket(basketComponent);
        };
    }
}
