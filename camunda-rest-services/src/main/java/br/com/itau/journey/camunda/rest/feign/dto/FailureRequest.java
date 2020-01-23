package br.com.itau.journey.camunda.rest.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FailureRequest {

    private String workerId;
    private String errorMessage;
    private Long retries;
    private Long retryTimeout;
    
}
