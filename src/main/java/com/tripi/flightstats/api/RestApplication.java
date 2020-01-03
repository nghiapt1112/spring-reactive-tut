package com.tripi.flightstats.api;

import com.tripi.flightstats.domain.demo.FlightArrivalResponse;
import com.tripi.flightstats.domain.demo.Product;
import com.tripi.flightstats.domain.demo.RecentPurchaseResponse;
import com.tripi.flightstats.domain.demo.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RestApplication {

    private PurchaseService purchaseService;

    @Autowired
    public RestApplication(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping(value = "/recent_purchases/{userName}")
    @Cacheable(value = "reservationsCache", key = "#userName")
    public Flux<RecentPurchaseResponse> get2(@PathVariable String userName) {
        return purchaseService.findRecentlyPurchase(userName);
    }

    @GetMapping
    public Mono<String> createRule() {
        return Mono.just("create-Rule_" + UUID.randomUUID().toString());
    }

    @GetMapping("/product2")
    public Mono<Product> getProduct() {
        return purchaseService.getProduct2();
    }

    @GetMapping("/product3")
    public Mono<FlightArrivalResponse> getProduct3() {
        return purchaseService.getProduct3();
    }
}
