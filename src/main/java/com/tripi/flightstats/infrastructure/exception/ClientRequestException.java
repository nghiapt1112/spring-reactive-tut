package com.tripi.flightstats.infrastructure.exception;

import org.springframework.web.reactive.function.client.WebClientException;

public class ClientRequestException extends WebClientException {
    public ClientRequestException(String msg) {
        super(msg);
    }

    public ClientRequestException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
