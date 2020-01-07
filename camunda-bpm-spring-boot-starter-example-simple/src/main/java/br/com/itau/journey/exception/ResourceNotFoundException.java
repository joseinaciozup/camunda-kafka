package br.com.itau.journey.exception;

import feign.Request;

public final class ResourceNotFoundException extends BaseIntegrationException {
   public ResourceNotFoundException(String message, String responseBody, Request.HttpMethod httpMethod) {
      super(message, responseBody, httpMethod);
   }
}
