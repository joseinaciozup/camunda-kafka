package br.com.itau.journey.exception;

import java.util.Date;

import feign.Request;
import feign.RetryableException;

public abstract class BaseIntegrationException extends RetryableException {

    private final String responseBody;


    public BaseIntegrationException(String message, String responseBody, Request.HttpMethod httpMethod) {
        super(message, httpMethod, new Date());
        this.responseBody = responseBody;
    }

    public final String getResponseBody() {
        return this.responseBody;
    }
}
