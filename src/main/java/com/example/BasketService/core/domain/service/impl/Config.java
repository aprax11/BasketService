package com.example.BasketService.core.domain.service.impl;

import com.example.BasketService.core.domain.model.BasketComponent;
import com.example.BasketService.core.domain.model.Product;
import com.example.BasketService.core.domain.service.interfaces.IBasketRepository;
import com.example.BasketService.core.domain.service.interfaces.IBasketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class Config {
    @Bean
    CommandLineRunner commandLineRunner(IBasketRepository basketRepository, IBasketService basketService){
        return args -> {
            Product product1 = new Product(
                    UUID.randomUUID(),
                    "Ring",
                    "22€",
                    "1"
            );
            Product product2 = new Product(
                    UUID.randomUUID(),
                    "Kette",
                    "22€",
                    "1"
            );


            basketService.createProduct(product1);
            UUID user = UUID.randomUUID();
            BasketComponent basketComponent = new BasketComponent(user, product1);
//            basketService.createProduct(product2);
//            BasketComponent basketComponent2 = new BasketComponent(user, product2);
            basketService.addToBasket(basketComponent);
            basketService.addToBasket(basketComponent);
            log.info(String.valueOf(basketRepository.findAll()
                    .stream()
                    .filter(productEntity -> productEntity.getUserId().compareTo(basketComponent.getUserId())== 0)
                    .collect(Collectors.toList()).get(0)));
//            basketService.addToBasket(basketComponent2);
        };
    }
}
