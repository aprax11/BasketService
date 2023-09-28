package com.example.BasketService;

import com.example.BasketService.core.domain.model.BasketComponent;
import com.example.BasketService.core.domain.model.BasketEntity;
import com.example.BasketService.core.domain.model.BasketItem;
import com.example.BasketService.core.domain.model.Product;
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

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void deleteFromBasketTest(){

        UUID testUUID = UUID.randomUUID();
        UUID testUserUUID = UUID.randomUUID();

        Product testProduct1 = new Product( testUUID, "testProduct1", "22€", "1");

        List<Product> testProductList = List.of(testProduct1, testProduct1);

        BasketEntity testBasketEntity = new BasketEntity(testUserUUID, testProductList);

        when(basketRepository.findAll()).thenReturn(List.of(testBasketEntity));

        BasketComponent testBasketComponent = new BasketComponent(testUserUUID, testProduct1);

        basketService.deleteFromBasket(testBasketComponent);

        ArgumentCaptor<BasketEntity> argumentCaptor = ArgumentCaptor.forClass(BasketEntity.class);
        verify(basketRepository).save(argumentCaptor.capture());

        BasketEntity capturedBasketEntity = argumentCaptor.getValue();

        assertEquals(1, capturedBasketEntity.getProducts().size());
    }
}
