package com.example.BasketService.port.listener;

import com.example.BasketService.core.domain.model.BasketComponent;
import com.example.BasketService.core.domain.model.Product;
import com.example.BasketService.core.domain.service.impl.BasketService;
import com.example.BasketService.port.listener.Listener;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.util.UUID;

import static com.example.BasketService.port.listener.MessageType.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListenerTest {

    @Mock
    private BasketService basketService;
    @Mock
    private Message message;
    @Mock
    private MessageProperties messageProperties;
    @InjectMocks
    private Listener listener;

    @BeforeEach
    void setUp() {
        when(message.getMessageProperties()).thenReturn(messageProperties);
    }
    @Test
    void handleRequestDeleteProductTest() {
        when(messageProperties.getType()).thenReturn(DELETE_PRODUCT.name());
        when(message.getBody()).thenReturn(UUID.randomUUID().toString().getBytes());

        listener.handleRequest(message);

        verify(basketService).deleteProduct(any(UUID.class));
    }
    @Test
    void handleRequestUpdateProductTest() {
        when(messageProperties.getType()).thenReturn(UPDATE_PRODUCT.name());
        when(message.getBody()).thenReturn(new Gson().toJson(new Product()).getBytes());

        listener.handleRequest(message);

        verify(basketService).updateProduct(any(Product.class));
    }
    @Test
    void handleRequestCreateProductTest() {
        when(messageProperties.getType()).thenReturn(CREATE_PRODUCT.name());
        when(message.getBody()).thenReturn(new Gson().toJson(new Product()).getBytes());

        listener.handleRequest(message);

        verify(basketService).createProduct(any(Product.class));
    }
    @Test
    void handleRequestAddToBasketTest() {
        when(messageProperties.getType()).thenReturn(ADD_TO_BASKET.name());
        when(message.getBody()).thenReturn(new Gson().toJson(new BasketComponent()).getBytes());

        listener.handleRequest(message);

        verify(basketService).addToBasket(any(BasketComponent.class));
    }
    @Test
    void handleRequestDeleteFromBasketTest() {
        when(messageProperties.getType()).thenReturn(DELETE_FROM_BASKET.name());
        when(message.getBody()).thenReturn(new Gson().toJson(new BasketComponent()).getBytes());

        listener.handleRequest(message);

        verify(basketService).deleteFromBasket(any(BasketComponent.class));
    }
    @Test
    void handleRequestGetBasketTest() {
        when(messageProperties.getType()).thenReturn(GET_BASKET.name());
        when(message.getBody()).thenReturn(UUID.randomUUID().toString().getBytes());

        listener.handleRequest(message);

        verify(basketService).getBasketFromUser(any(UUID.class));
    }



    @Test
    void handleRequestInvalidType() {
        when(messageProperties.getType()).thenReturn("invalid type");

        listener.handleRequest(message);

        verifyNoInteractions(basketService);
    }
}
