package com.example.BasketService.port.listener;

import com.example.BasketService.core.domain.model.BasketComponent;
import com.example.BasketService.core.domain.model.Product;
import com.example.BasketService.core.domain.model.Basket;
import com.example.BasketService.core.domain.service.interfaces.IBasketService;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.ErrorResponseException;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Data
@Controller
public class Listener {
    private final IBasketService basketService;
    @RabbitListener(queues = {"basket-service.rpc.queue"})
    public String handleRequest(Message message){
        log.info("receiveMessage triggered");
        final MessageType messageType;
        try {
            messageType = MessageType.valueOf(message.getMessageProperties().getType());
        } catch (IllegalArgumentException e) {
            return logInvalidMessageType(message.getMessageProperties().getType());
        }
        try{
            switch (messageType){
                case CREATE_PRODUCT: {
                    Product product = createProductFromMessage(message);
                    log.info("create call processed");
                    return createProduct(product);
                }
                case UPDATE_PRODUCT: {
                    Product product = createProductFromMessage(message);
                    log.info("update product request processed");
                    return updateProduct(product);
                }
                case DELETE_PRODUCT: {
                    UUID id = extractIdFromMessage(message);
                    log.info("delete product request processed");
                    return deleteProduct(id);
                }
                case GET_BASKET: {
                    UUID username = extractIdFromMessage(message);
                    log.info("getBasket request processed");
                    return getBasket(username);
                }
                case ADD_TO_BASKET: {
                    log.info("add component to Basket request processed");
                    BasketComponent basketComponent = createBasketComponentFromMessage(message);
                    log.info("got basket component");
                    return addToBasket(basketComponent);
                }
                case DELETE_FROM_BASKET: {
                    BasketComponent basketComponent = createBasketComponentFromMessage(message);
                    log.info("delete from basket request processed");
                    return deleteFromBasket(basketComponent);
                }
                default: {
                    return errorResponse();
                }
            }
        } catch (ErrorResponseException e) {
            return errorResponse();
        }
    }

    private String deleteProduct(UUID id) {
        String answer = basketService.deleteProduct(id);
        return answer;
    }
    private String updateProduct(Product product) {
        Product updatedProduct = basketService.updateProduct(product);
        return new Gson().toJson(updatedProduct);
    }
    private String createProduct(Product product) {
        Product createdProduct = basketService.createProduct(product);
        return new Gson().toJson(createdProduct);
    }
    private String getBasket(UUID username){
        Basket basket = basketService.getBasketFromUser(username);
        return new Gson().toJson(basket);
    }
    private String addToBasket(BasketComponent basketComponent){
        return new Gson().toJson(basketService.addToBasket(basketComponent));
    }
    private String deleteFromBasket(BasketComponent basketComponent){
        return new Gson().toJson(basketService.deleteFromBasket(basketComponent));
    }
    private String extractBodyOfMessage(Message message) {
        return new String(message.getBody(), StandardCharsets.UTF_8);
    }
    private UUID extractIdFromMessage(Message message) {
        return UUID.fromString(extractBodyOfMessage(message));
    }
    private Product createProductFromMessage(Message message) {
        return new Gson().fromJson(extractBodyOfMessage(message), Product.class);
    }
    private BasketComponent createBasketComponentFromMessage(Message message) {
        BasketComponent gotComponent = new Gson().fromJson(extractBodyOfMessage(message), BasketComponent.class);
        return gotComponent;
    }
    private String errorResponse() {
        log.error("respond with message 'errorResponse'");
        return "errorResponse";
    }
    private String logInvalidMessageType(String type) {
        log.info("invalid message type: " + type);
        return errorResponse();
    }
}
