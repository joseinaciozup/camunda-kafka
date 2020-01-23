package br.com.itau.journey.feign.decoder;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import br.com.itau.journey.feign.exception.IntegrationErrorException;
import br.com.itau.journey.feign.exception.ResourceNotFoundException;
import feign.Request;
import feign.Response;
import feign.codec.ErrorDecoder;

@Component
public class IntegrationErrorDecoder implements ErrorDecoder {

    public Exception decode(String methodKey, Response response) {

        if (response != null) {
            String responseBody = null;

            if (response.body() != null) {
                try {
                    responseBody = IOUtils.toString(response.body().asInputStream());
                } catch (IOException e) {
                    responseBody = null;
                }
            }

            if (response.status() == HttpStatus.NOT_FOUND.value()) {
                return new ResourceNotFoundException(response.reason(), responseBody, response.request().httpMethod());
            }

            return new IntegrationErrorException(response.reason(), responseBody, response.request().httpMethod());
        }

        return new IntegrationErrorException("Unknow error", StringUtils.EMPTY, Request.HttpMethod.OPTIONS);
    }
}
