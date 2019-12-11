package br.com.itau.journey.feign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CompleteTaskRequest {

    private String workerId;

    private Map variables = new LinkedHashMap<>();

}
