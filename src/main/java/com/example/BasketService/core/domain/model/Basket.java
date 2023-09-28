package com.example.BasketService.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Basket {

    private UUID userId;

    private List<BasketItem> products;

    private int totalPrice;

    public Map<UUID, Integer> getCountsOfItems(){

        Map<UUID, Integer> retMap = new HashMap<>();
        for (BasketItem basketItem: products
             ) {
            retMap.put(basketItem.getId(), basketItem.getCount());
        }
        return retMap;
    }
    public void setCountForItem(UUID itemId, int newCount){
        for (BasketItem basketItem: products
             ) {
            if(basketItem.getId().compareTo(itemId) == 0){
                basketItem.setCount(newCount);
            }
        }
        recalculatePrice();
    }
    public static int parsePriceOfItem(String price){

        int ret = 0;

        try{
            ret = Integer.parseInt(price.substring(0, price.length() - 1));
        }catch (Exception e)
        {
            log.error("unexpected error during price calculation");
        }
        return ret;
    }
    private void recalculatePrice(){
        int newTotalPrice = 0;

        for (BasketItem item: products
             ) {
            newTotalPrice += parsePriceOfItem(item.getPrice());
        }
        this.totalPrice = newTotalPrice;
    }
}
