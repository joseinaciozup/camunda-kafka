package br.com.itau.journey.camunda.rest.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestControllerStartDTO {

    private String time;
    private String direction;
}
