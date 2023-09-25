package com.example.BasketService.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasketComponent {

     private UUID username;

     private Product product;

     public UUID getProductID(){
          UUID ret = product.getId();
          return ret;
     }
}
