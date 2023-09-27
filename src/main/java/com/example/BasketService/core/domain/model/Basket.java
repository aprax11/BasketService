package com.example.BasketService.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Basket {

    private UUID userId;

    private List<Product> products;

    private int totalPrice;


}
