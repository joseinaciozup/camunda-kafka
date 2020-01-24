package br.com.itau.journey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestStartDTO {

    private String bpmnInstance;
    private boolean sync;
    private VariablesStartDTO variables;
}
