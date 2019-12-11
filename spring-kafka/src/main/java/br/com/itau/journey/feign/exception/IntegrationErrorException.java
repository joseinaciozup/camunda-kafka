package br.com.itau.journey.feign.exception;

import feign.Request;

public final class IntegrationErrorException extends BaseIntegrationException {
   
   private final String message;

   
   public String getMessage() {
      return this.message;
   }

   public IntegrationErrorException(String message, String responseBody, Request.HttpMethod httpMethod) {
      super(message, responseBody, httpMethod);
      this.message = message;
   }
}
