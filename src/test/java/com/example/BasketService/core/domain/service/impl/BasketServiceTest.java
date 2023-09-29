package com.example.BasketService.core.domain.service.impl;

import com.example.BasketService.core.domain.model.*;
import com.example.BasketService.core.domain.service.impl.BasketService;
import com.example.BasketService.core.domain.service.interfaces.IBasketRepository;
import com.example.BasketService.core.domain.service.interfaces.IProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BasketServiceTest {

    @Mock
    private IBasketRepository basketRepository;
    @Mock
    private IProductRepository productRepository;
    @InjectMocks
    private BasketService basketService;

    private final UUID TEST_PRODUCT_UUID1 = UUID.fromString("514baee2-b42a-4f0d-9eba-8e82d2db968b");
    private final Product TEST_PRODUCT1 = new Product(TEST_PRODUCT_UUID1, "testProduct1", "22", "1");
    private final UUID TEST_PRODUCT_UUID2 = UUID.fromString("923214f8-5111-481c-ba29-86dfe1c2fa0a");
    private final Product TEST_PRODUCT2 = new Product(TEST_PRODUCT_UUID2, "testProduct2", "12", "2");
    private final String DELETE_RESPONSE = "deleting item";
    private final UUID TEST_USER_ID = UUID.fromString("d6e38053-872d-4d4a-b9e5-ce48cd749e62");


    @Test
    void createProductTest(){
        Product returnProduct = basketService.createProduct(TEST_PRODUCT1);

        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(argumentCaptor.capture());

        Product capturedProduct = argumentCaptor.getValue();

        assertEquals(TEST_PRODUCT1, capturedProduct);

        assertEquals(TEST_PRODUCT1, returnProduct);
    }
    @Test
    void updateProductTest(){

        when(productRepository.existsById(TEST_PRODUCT_UUID1)).thenReturn(true);

        Product returnProduct = basketService.updateProduct(TEST_PRODUCT1);

        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(argumentCaptor.capture());

        Product capturedProduct = argumentCaptor.getValue();

        assertEquals(TEST_PRODUCT1, capturedProduct);

        assertEquals(TEST_PRODUCT1, returnProduct);
    }
    @Test
    void deleteProductTest(){

        when(productRepository.existsById(TEST_PRODUCT_UUID1)).thenReturn(true);

        String returnString = basketService.deleteProduct(TEST_PRODUCT_UUID1);

        ArgumentCaptor<UUID> argumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(productRepository).deleteById(argumentCaptor.capture());

        UUID capturedId = argumentCaptor.getValue();

        assertEquals(TEST_PRODUCT_UUID1, capturedId);

        assertEquals(DELETE_RESPONSE, returnString);
    }
    @Test
    void createEmptyBasketForUserTest(){

        basketService.createEmptyBasketForUser(TEST_PRODUCT_UUID1);

        ArgumentCaptor<BasketEntity> argumentCaptor = ArgumentCaptor.forClass(BasketEntity.class);
        verify(basketRepository).save(argumentCaptor.capture());

        BasketEntity capturedEntity = argumentCaptor.getValue();

        assertEquals(TEST_PRODUCT_UUID1, capturedEntity.getUserId());

        assertTrue(capturedEntity.getProducts().isEmpty());
    }
    @Test
    void addToBasketUserExistsTest(){

        List<Product> testProductList = new ArrayList<>();

        testProductList.add(TEST_PRODUCT1);

        BasketEntity testBasketEntity = new BasketEntity(TEST_USER_ID, testProductList);

        when(basketRepository.findAll()).thenReturn(List.of(testBasketEntity));

        when(basketRepository.existsById(TEST_USER_ID)).thenReturn(true);

        BasketComponent testBasketComponent = new BasketComponent(TEST_USER_ID, TEST_PRODUCT1);

        basketService.addToBasket(testBasketComponent);

        ArgumentCaptor<BasketEntity> argumentCaptor = ArgumentCaptor.forClass(BasketEntity.class);
        verify(basketRepository).save(argumentCaptor.capture());

        BasketEntity capturedBasketEntity = argumentCaptor.getValue();

        assertEquals(TEST_USER_ID, capturedBasketEntity.getUserId());
        assertEquals(2, capturedBasketEntity.getProducts().size());
        assertEquals(TEST_PRODUCT1, capturedBasketEntity.getProducts().get(0));
        assertEquals(TEST_PRODUCT1, capturedBasketEntity.getProducts().get(1));
    }
    @Test
    void addToBasketUserNotExistsTest(){

        when(basketRepository.existsById(TEST_USER_ID)).thenReturn(false);

        BasketComponent testBasketComponent = new BasketComponent(TEST_USER_ID, TEST_PRODUCT1);

        basketService.addToBasket(testBasketComponent);

        ArgumentCaptor<BasketEntity> argumentCaptor = ArgumentCaptor.forClass(BasketEntity.class);
        verify(basketRepository).save(argumentCaptor.capture());

        BasketEntity capturedBasketEntity = argumentCaptor.getValue();

        assertEquals(TEST_USER_ID, capturedBasketEntity.getUserId());
        assertEquals(1, capturedBasketEntity.getProducts().size());
        assertEquals(TEST_PRODUCT1, capturedBasketEntity.getProducts().get(0));
    }
    @Test
    void getBasketFromUserTest(){

        List<Product> testProductList = new ArrayList<>();

        testProductList.add(TEST_PRODUCT1);

        BasketEntity testBasketEntity = new BasketEntity(TEST_USER_ID, testProductList);

        when(basketRepository.findAll()).thenReturn(List.of(testBasketEntity));

        when(basketRepository.existsById(TEST_USER_ID)).thenReturn(true);

        Basket returnBasket = basketService.getBasketFromUser(TEST_USER_ID);



        assertEquals(TEST_USER_ID, returnBasket.getUserId());
        assertEquals(1, returnBasket.getProducts().size());
        assertEquals(BasketItem.class, returnBasket.getProducts().get(0).getClass());
    }

    @Test
    void deleteFromBasketTest(){

        List<Product> testProductList = List.of(TEST_PRODUCT1, TEST_PRODUCT1);

        BasketEntity testBasketEntity = new BasketEntity(TEST_USER_ID, testProductList);

        when(basketRepository.findAll()).thenReturn(List.of(testBasketEntity));

        BasketComponent testBasketComponent = new BasketComponent(TEST_USER_ID, TEST_PRODUCT1);

        String retString = basketService.deleteFromBasket(testBasketComponent);

        ArgumentCaptor<BasketEntity> argumentCaptor = ArgumentCaptor.forClass(BasketEntity.class);
        verify(basketRepository).save(argumentCaptor.capture());

        BasketEntity capturedBasketEntity = argumentCaptor.getValue();

        assertEquals(1, capturedBasketEntity.getProducts().size());
        assertEquals(TEST_PRODUCT1, capturedBasketEntity.getProducts().get(0));
        assertEquals(DELETE_RESPONSE, retString);
    }
    @Test
    void fromEntityAggregationTest() {

        List<Product> testProductList = List.of(TEST_PRODUCT1, TEST_PRODUCT1);

        BasketEntity testBasketEntity = new BasketEntity(TEST_USER_ID, testProductList);

        BasketItem expectedBasketItem = new BasketItem(TEST_PRODUCT_UUID1, TEST_PRODUCT1.getName()
                , TEST_PRODUCT1.getPrice()
                , TEST_PRODUCT1.getImage()
                , 2);

        int price = Integer.parseInt(TEST_PRODUCT1.getPrice());

        Basket expectedBasket = new Basket(TEST_USER_ID, List.of(expectedBasketItem),price*2);

        Basket returnBasket = basketService.fromEntity(testBasketEntity);

        assertEquals(expectedBasket, returnBasket);
    }
    @Test
    void fromEntityDifferentProductsTest() {

        List<Product> testProductList = List.of(TEST_PRODUCT1, TEST_PRODUCT2);

        BasketEntity testBasketEntity = new BasketEntity(TEST_USER_ID, testProductList);

        BasketItem expectedBasketItem1 = new BasketItem(TEST_PRODUCT_UUID1, TEST_PRODUCT1.getName()
                , TEST_PRODUCT1.getPrice()
                , TEST_PRODUCT1.getImage()
                , 1);

        BasketItem expectedBasketItem2 = new BasketItem(TEST_PRODUCT_UUID2, TEST_PRODUCT2.getName()
                , TEST_PRODUCT2.getPrice()
                , TEST_PRODUCT2.getImage()
                , 1);

        int price1 = Integer.parseInt(TEST_PRODUCT1.getPrice());
        int price2 = Integer.parseInt(TEST_PRODUCT2.getPrice());

        Basket expectedBasket = new Basket(TEST_USER_ID, List.of(expectedBasketItem1, expectedBasketItem2),price1+price2);

        Basket returnBasket = basketService.fromEntity(testBasketEntity);

        assertEquals(expectedBasket, returnBasket);
    }
    @Test
    void toEntityFromAggregatedProductTest() {

        BasketItem expectedBasketItem = new BasketItem(TEST_PRODUCT_UUID1, TEST_PRODUCT1.getName()
                , TEST_PRODUCT1.getPrice()
                , TEST_PRODUCT1.getImage()
                , 2);

        int price = Integer.parseInt(TEST_PRODUCT1.getPrice().substring(0, TEST_PRODUCT1.getPrice().length() - 1));

        Basket basket = new Basket(TEST_USER_ID, List.of(expectedBasketItem),price*2);

        BasketEntity returnBasketEntity = basketService.toEntity(basket);

        List<Product> testProductList = List.of(TEST_PRODUCT1, TEST_PRODUCT1);
        BasketEntity expectetBasketEntity = new BasketEntity(TEST_USER_ID, testProductList);

        assertEquals(expectetBasketEntity, returnBasketEntity);
    }
    @Test
    void toEntityFromDifferentProductsTest() {

        BasketItem basketItem1 = new BasketItem(TEST_PRODUCT_UUID1, TEST_PRODUCT1.getName()
                , TEST_PRODUCT1.getPrice()
                , TEST_PRODUCT1.getImage()
                , 1);

        BasketItem basketItem2 = new BasketItem(TEST_PRODUCT_UUID2, TEST_PRODUCT2.getName()
                , TEST_PRODUCT2.getPrice()
                , TEST_PRODUCT2.getImage()
                , 1);

        int price1 = Integer.parseInt(TEST_PRODUCT1.getPrice().substring(0, TEST_PRODUCT1.getPrice().length() - 1));
        int price2 = Integer.parseInt(TEST_PRODUCT2.getPrice().substring(0, TEST_PRODUCT2.getPrice().length() - 1));

        Basket basket = new Basket(TEST_USER_ID, List.of(basketItem1, basketItem2),price1+price2);

        BasketEntity returnBasketEntity = basketService.toEntity(basket);

        List<Product> testProductList = List.of(TEST_PRODUCT1, TEST_PRODUCT2);
        BasketEntity expectedBasketEntity = new BasketEntity(TEST_USER_ID, testProductList);

        assertEquals(expectedBasketEntity, returnBasketEntity);
    }


}
