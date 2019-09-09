package com.example.demo.service;

import com.example.demo.infrastructure.service.BaseService;
import com.example.demo.model.RecentPurchaseResponse;
import com.example.demo.model.Product;
import com.example.demo.model.ProductResponse;
import com.example.demo.model.PurchaseData;
import com.example.demo.model.PurchaseResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.model.PurchaseResponse.throwIfEmpty;

@Service
public class PurchaseService extends BaseService {

    public Mono<List<RecentPurchaseResponse>> findRecentlyPurchase(String userName) {
        System.out.println("\n\nCalling to API: " + "-findRecentlyPurchase-" + "\n\n");
        return getRecentPurchasesByUser(userName)
                .flatMap(purchaseResponse ->
                        Flux.zip(
                                getProductInfo(purchaseResponse.getProductId()),
                                getAllPeopleWhoPreviouslyPurchased(purchaseResponse.getProductId())
                                        .map(previouslyPurchase -> previouslyPurchase.getUsername())
                                        .collectList(),
                                (product, responseFlux) -> new RecentPurchaseResponse(product, responseFlux))
                )
                .collectList()
                .map(val ->
                        val.parallelStream()
                                .sorted((o1, o2) -> o2.getRecent().size() - o1.getRecent().size())
                                .collect(Collectors.toList())
                );
    }

    public Flux<PurchaseData> getRecentPurchasesByUser(String userName) {
        return get(RECENT_PURCHASES_BY_USER, userName, PurchaseResponse.class)
                .doOnNext(recentPurchaseResponse -> throwIfEmpty(recentPurchaseResponse, userName))
                .flatMapMany(recentPurchaseResponse -> Flux.fromIterable(recentPurchaseResponse.getPurchases()));
    }

    public Flux<PurchaseData> getAllPeopleWhoPreviouslyPurchased(Integer productId) {
        return get(ALL_PEOPLE_WHO_PREVIOUSLY_PURCHASED, productId, PurchaseResponse.class)
                .flatMapMany(recentPurchaseResponse -> Flux.fromIterable(recentPurchaseResponse.getPurchases()));
    }

    public Mono<Product> getProductInfo(Integer productId) {
        return get(PRODUCT_INFO, productId, ProductResponse.class)
                .flatMap(recentPurchaseResponse -> Mono.just(recentPurchaseResponse.getProduct()));
    }

}
