package br.com.itau.journey.camunda.rest.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CompleteTaskRequest {

    private String workerId;

    private Map<String, Value> variables;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class Value {
        private String value;
    }

}
