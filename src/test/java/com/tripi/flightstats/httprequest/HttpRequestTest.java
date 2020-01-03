package com.tripi.flightstats.httprequest;

import com.tripi.flightstats.ApplicationTest;
import com.tripi.flightstats.domain.demo.Product;
import com.tripi.flightstats.infrastructure.exception.ClientRequestException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class HttpRequestTest extends ApplicationTest {
    private final Logger log = LoggerFactory.getLogger("Test");
    public WebClient client3;

    @Value("${app.external.host}")
    private String host;
    @Value("${flightstats.create.rule_by_arrival}")
    private String ruleByArrival;

    @Test
    public void test2() {
        WebClient
                .builder()
                .filters(exchange -> {
                    exchange.add(logRequest());
                    exchange.add(logResponse());
                })
                .baseUrl("https://www.google.com/")
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build()
                .get()
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(err -> System.out.println("=>>>> ERROR"))
                .doOnSubscribe(sub -> {
                    System.out.println("=>>>>>>>>>>sub");
                })
                .subscribe(s -> System.out.println("=>>>>>>>>>>>>>>>>>>>s"));
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            if (log.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Request: \n")
                        .append(clientRequest.method())
                        .append(" ")
                        .append(clientRequest.url());
                clientRequest
                        .headers()
                        .forEach((name, values) -> values.forEach(value -> sb
                                .append("\n")
                                .append(name)
                                .append(":")
                                .append(value)));
                log.debug(sb.toString());
            }
            return Mono.just(clientRequest);
        });
    }


    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (log.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Response: \n")
                        .append("Status: ")
                        .append(clientResponse.rawStatusCode());
                clientResponse
                        .headers()
                        .asHttpHeaders()
                        .forEach((key, value1) -> value1.forEach(value -> sb
                                .append("\n")
                                .append(key)
                                .append(":")
                                .append(value)));
                log.debug(sb.toString());
            }
            return Mono.just(clientResponse);
        });
    }

    @Test
    public void get() {
        try {
            init();
//            myTest()
            get(ruleByArrival, "AA", String.class)
                    .doOnError(err -> System.out.println("=================Loi roi=================" + err.getMessage()))
                    .subscribe(recentPurchaseResponse -> print(recentPurchaseResponse))
            ;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            this.client3 = null;
        }
    }

    @Test
    public void myTest() {
        client3 = WebClient
                .builder()
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .baseUrl("http://localhost:8080/api/product")
                .build();
        client3.get()
                .retrieve()
                .bodyToMono(Product.class)
                .doOnError(err -> {
                    System.out.println("==================ERROR");
                })
                .doOnSubscribe(res -> {
                    Mono.just(res);
                })
                .subscribe(sub ->{
                    System.out.println("SUBBBBBBBBBBBBBBBBBBBBb");
                });
    }

    public void print(Object s) {
        System.out.println(s);
    }

    public void init() {
        client3 = WebClient
                .builder()
                .baseUrl(host)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * @param api:   api name
     * @param param: param as a single request param (not supported for array type)
     * @param typed: type of response object
     * @param <O>
     * @return
     */
    public <O> Mono<O> get(String api, Object param, Class<O> typed) {
        return client3
                .get()
//                .uri(uriBuilder -> uriBuilder
//                        .path(api)
//                        .build(param)
//                )
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
                .flatMap(clientResponse -> {
                    System.out.println("===================Parsing data");
                    return clientResponse.bodyToMono(typed);
                });
    }

}
