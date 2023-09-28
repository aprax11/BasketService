package com.example.BasketService.core.domain.service.impl;

import com.example.BasketService.core.domain.model.*;
import com.example.BasketService.core.domain.service.Statics;
import com.example.BasketService.core.domain.service.interfaces.IBasketRepository;
import com.example.BasketService.core.domain.service.interfaces.IBasketService;
import com.example.BasketService.core.domain.service.interfaces.IProductRepository;
import com.example.BasketService.exceptions.ItemDoesNotExistException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class BasketService implements IBasketService {

    private final IProductRepository productRepository;

    private final IBasketRepository basketRepository;

    @Override
    public Product createProduct(Product product) {

        log.info("saving Product: {}", product);

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
    public void createEmptyBasketForUser(UUID userId){

        BasketEntity basketEntity = new BasketEntity(userId, new ArrayList<Product>());

        basketRepository.save(basketEntity);
    }
    @Override
    public BasketComponent addToBasket(BasketComponent basketComponent) {

        log.info("adding component to basket: {}", basketComponent);

        UUID userId = basketComponent.getUserId();
        BasketEntity basketEntity = new BasketEntity();

        if(isExistsBasket(userId)){
            basketEntity = filterResultByUser(basketComponent.getUserId());

            List<Product> products = basketEntity.getProducts();

            products.add(basketComponent.getProduct());

            basketEntity.setProducts(products);

        }else {
            List<Product> products = new ArrayList<>();
            products.add(basketComponent.getProduct());

            basketEntity.setUserId(userId);
            basketEntity.setProducts(products);
        }
        basketRepository.save(basketEntity);

        return basketComponent;
    }
    @Override
    public String deleteFromBasket(BasketComponent basketComponent) {

        log.info("deleting component: {} from basket", basketComponent);

        BasketEntity basketEntity = filterResultByUser(basketComponent.getUserId());

        Basket basket = fromEntity(basketEntity);

        Map<UUID, Integer> counts = basket.getCountsOfItems();

        int newCount = counts.get(basketComponent.getProductID()) - 1;

        basket.setCountForItem(basketComponent.getProductID(), newCount);

        basketRepository.save(toEntity(basket));

        return Statics.DELETE_RESPONSE;
    }
    @Override
    public Basket getBasketFromUser(UUID userId){

        log.info("getting basket for user: {}", userId);

        if(isExistsBasket(userId)){
            log.info("tying to get basket");
            Basket ret = fromEntity(filterResultByUser(userId));

            return ret;
        }else{
            throw new ItemDoesNotExistException();
        }
    }
    @Override
    public Basket fromEntity(BasketEntity entity){

        int price = 0;

        Map<String, BasketItem> productsMap = new HashMap<>();

        for (Product p: entity.getProducts()) {

            int itemPrice = Basket.parsePriceOfItem(p);
            price += itemPrice;

            String key = p.getId().toString();
            if(productsMap.containsKey(key)){

                BasketItem item = productsMap.get(p.getId().toString());
                item.setCount(item.getCount()+1);

                productsMap.put(key, item);
            }else{

                BasketItem newItem = new BasketItem(p.getId(), p.getName(), p.getPrice(), p.getImage(), 1);
                productsMap.put(key, newItem);
            }
        }
        return new Basket(entity.getUserId(), productsMap.values().stream().toList(), price);
    }
    @Override
    public BasketEntity toEntity(Basket basket){

        List<Product> products = new ArrayList<>();

        for (BasketItem basketItem: basket.getProducts()
             ) {
            Product product = new Product(basketItem.getId(), basketItem.getName(), basketItem.getPrice(), basketItem.getImage());
            for (int i = 0; i < basketItem.getCount(); i++){
                products.add(product);
            }
        }
        return new BasketEntity(basket.getUserId(), products);
    }

    private BasketEntity filterResultByUser(UUID userId){

        return basketRepository.findAll()
                .stream()
                .filter(productEntity -> productEntity.getUserId().compareTo(userId)== 0)
                .toList().get(0);
    }
    private boolean isExistsProduct(UUID id) {
        return productRepository.existsById(id);
    }
    private boolean isExistsBasket(UUID username) {
        return basketRepository.existsById(username);
    }
}
