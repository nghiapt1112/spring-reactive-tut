package com.tripi.flightstats.domain.demo.service;

import com.tripi.flightstats.domain.demo.*;
import com.tripi.flightstats.infrastructure.exception.ClientRequestException;
import com.tripi.flightstats.infrastructure.service.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.UUID;

import static com.tripi.flightstats.domain.demo.PurchaseResponse.throwIfEmpty;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
public class PurchaseService extends BaseService {

    public Flux<RecentPurchaseResponse> findRecentlyPurchase(String userName) {
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
//                .collectList()
                .flatMap(val ->
                                Flux.just(val).sort((o1, o2) -> o2.getRecent().size() - o1.getRecent().size())

//                        val.parallelStream()
//                                .sorted((o1, o2) -> o2.getRecent().size() - o1.getRecent().size())
//                                .collect(Collectors.toList())
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

    public Mono<Product> getProduct2() {
        String api = "http://localhost:8080/api/product";
        client3 = WebClient
                .builder()
                .baseUrl(api)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build();

        return client3
                .get()
//                .uri(uriBuilder -> uriBuilder
//                        .path(api)
//                        .build(param))
                .exchange()
                .doOnError(throwable -> {
                    throw new ClientRequestException(String.format("An error has occur when trying to access to:\nAPI: %s\n", api), throwable);
                })
                .doOnNext(response -> {
                    HttpStatus httpStatus = response.statusCode();
                    if (httpStatus.is4xxClientError() || httpStatus.is5xxServerError()) {
                        throw new ClientRequestException(String.format("An error has occur when trying to access to:\nAPI: %s\nStatus: %s\nReason: %s", api, httpStatus.value(), httpStatus.getReasonPhrase()));
                    }
                })
                .flatMap(clientResponse -> clientResponse.bodyToMono(Product.class));
    }

    public Mono<FlightArrivalResponse> getProduct3() {
//        String api = "http://localhost:8080/api/product";
//        String api = "https://api.flightstats.com/flex/alerts/rest/v1/json/create/AA/100/to/LHR/arriving/2020/01/20?appId=76e3cc5f&appKey=2a9e6f95fa525f42c03049cd6b25d111&type=JSON&deliverTo=http://your.post.url";
        String api = "https://api.flightstats.com/flex/alerts/rest";
        client3 = WebClient
                .builder()
                .baseUrl(api)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build();

        return client3
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/json/create/{carrier}/{flightNumber}/to/{arrivalAirport}/arriving/{year}/{month}/{day}")
                        .queryParam("appId", "76e3cc5f")
                        .queryParam("appKey", "2a9e6f95fa525f42c03049cd6b25d111")
                        .queryParam("type", "JSON")
                        .queryParam("deliverTo", "http://your.post.url")

                        .build("AA", 100, "LHR", 2020, 01, 20))
                .exchange()
                .doOnNext(response -> {
                    HttpStatus httpStatus = response.statusCode();
                    if (httpStatus.is4xxClientError() || httpStatus.is5xxServerError()) {
                        throw new ClientRequestException(String.format("An error has occur when trying to access to:\nAPI: %s\nStatus: %s\nReason: %s", api, httpStatus.value(), httpStatus.getReasonPhrase()));
                    }
                })
                .flatMap(clientResponse -> clientResponse.bodyToMono(FlightArrivalResponse.class))
                .doOnError(throwable -> {
                    throw new ClientRequestException(String.format("An error has occur when trying to access to:\nAPI: %s\n", api), throwable);
                });

    }
}
