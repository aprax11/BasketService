package com.example.BasketService;

import com.example.BasketService.core.domain.model.Basket;
import com.example.BasketService.core.domain.model.BasketItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BasketTest {

    private final UUID TEST_ITEM_UUID = UUID.randomUUID();
    private final UUID TEST_USER_ID = UUID.randomUUID();
    private final BasketItem TEST_BASKET_ITEM = new BasketItem(TEST_ITEM_UUID, "testBasketItem", "22€", "1", 2);


    @Test
    void getCountOfItemsTest(){

        Basket basket = new Basket(TEST_USER_ID, List.of(TEST_BASKET_ITEM), 44);

        Map<UUID, Integer> ret = basket.getCountsOfItems();

        assertEquals(ret.size(), 1);
        assertEquals(2, ret.get(TEST_ITEM_UUID));
    }
    @Test
    void setCountForItemTest(){

        Basket basket = new Basket(TEST_USER_ID, List.of(TEST_BASKET_ITEM), 44);

        basket.setCountForItem(TEST_ITEM_UUID, 1);

        assertEquals(1, basket.getProducts().size());
        assertEquals(22, basket.getTotalPrice());
    }
    @Test
    void parsePriceOfItemTest(){
        int price = Basket.parsePriceOfItem(TEST_BASKET_ITEM.getPrice());

        assertEquals(22, price);
    }

}
