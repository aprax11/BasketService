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

    private final UUID testItemUUID = UUID.randomUUID();
    private final UUID testBasketUUID = UUID.randomUUID();
    private final BasketItem testBasketItem = new BasketItem(testItemUUID, "testBasketItem", "22€", "1", 2);
    private Basket basket = new Basket(testBasketUUID, List.of(testBasketItem), 44);

    @Test
    void getCountOfItemsTest(){

        Map<UUID, Integer> ret = basket.getCountsOfItems();

        assertEquals(ret.size(), 1);
        assertEquals(2, ret.get(testItemUUID));
    }

}
