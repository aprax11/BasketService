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
    CommandLineRunner commandLineRunner( IBasketService basketService){
        return args -> {

            UUID user = UUID.fromString("d6e38053-872d-4d4a-b9e5-ce48cd749e62");
            basketService.createEmptyBasketForUser(user);

        };
    }
}
