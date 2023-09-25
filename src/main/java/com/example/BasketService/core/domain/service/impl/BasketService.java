package com.example.BasketService.core.domain.service.impl;

import com.example.BasketService.core.domain.model.Basket;
import com.example.BasketService.core.domain.model.BasketComponent;
import com.example.BasketService.core.domain.model.BasketEntity;
import com.example.BasketService.core.domain.model.Product;
import com.example.BasketService.core.domain.service.Statics;
import com.example.BasketService.core.domain.service.interfaces.IBasketRepository;
import com.example.BasketService.core.domain.service.interfaces.IBasketService;
import com.example.BasketService.core.domain.service.interfaces.IProductRepository;
import com.example.BasketService.exceptions.ItemDoesNotExistException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class BasketService implements IBasketService {

    private final IProductRepository productRepository;

    private final IBasketRepository basketRepository;

    @Override
    public Product createProduct(Product product) {

        log.info("saving Product: {}", product);

        product.setId(UUID.randomUUID());
        productRepository.save(product);
        return product;
    }
    @Override
    public Product updateProduct(Product product) {

        log.info("updating Product: {}", product);

        if(isExistsProduct(product.getId())){
            productRepository.save(product);
            return product;
        }else{
            throw new ItemDoesNotExistException();
        }
    }
    @Override
    public String deleteProduct(UUID id) {

        log.info("deleting Product with Id: {}", id);

        if(isExistsProduct(id)){
            productRepository.deleteById(id);
            return Statics.DELETE_RESPONSE;
        }else{
            throw new ItemDoesNotExistException();
        }
    }
    @Override
    public BasketComponent addToBasket(BasketComponent basketComponent) {

        log.info("adding component to basket: {}", basketComponent);

        String username = basketComponent.getUsername();
        BasketEntity basketEntity = new BasketEntity();

        if(isExistsBasket(username)){
            basketEntity = basketRepository.findBasketById(username).get();

            List<Product> products = basketEntity.getProducts();

            products.add(basketComponent.getProduct());

            basketEntity.setProducts(products);

        }else {
            List<Product> products = new ArrayList<>();
            products.add(basketComponent.getProduct());

            basketEntity.setUsername(username);
            basketEntity.setProducts(products);
        }
        basketRepository.save(basketEntity);

        return basketComponent;
    }
    @Override
    public String deleteFromBasket(BasketComponent basketComponent) {

        log.info("deleting component: {} from basket", basketComponent);

        BasketEntity basketEntity = basketRepository.findBasketById(basketComponent.getUsername()).get();

        List<Product> products = new ArrayList<>();

        for (Product p: basketEntity.getProducts()
             ) {
            if(p.getId().compareTo(basketComponent.getProductID()) != 0){
                products.add(p);
            }
        }

        if(products.isEmpty()){
            basketRepository.deleteById(basketComponent.getUsername());
        }else {
            basketEntity.setProducts(products);

            basketRepository.save(basketEntity);
        }
        return Statics.DELETE_RESPONSE;
    }
    @Override
    public Basket getBasketFromUser(String username){

        log.info("getting basket for user: {}", username);

        if(isExistsBasket(username)){

            Basket ret = fromEntity(basketRepository.findBasketById(username).get());

            return ret;
        }else{
            throw new ItemDoesNotExistException();
        }
    }
    @Override
    public Basket fromEntity(BasketEntity entity){
        int price = 0;
        for (Product p: entity.getProducts()) {
            price += p.getPrice();
        };
        return new Basket(entity.getUsername(), entity.getProducts(), price);
    }
    private boolean isExistsProduct(UUID id) {
        return productRepository.existsById(id);
    }
    private boolean isExistsBasket(String username) {
        return basketRepository.existsById(username);
    }
}
