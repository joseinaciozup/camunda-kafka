package br.com.itau.worker.dto;

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
    private VariablesStartDTO variables;
}
