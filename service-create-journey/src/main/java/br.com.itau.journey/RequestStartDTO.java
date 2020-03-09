package br.com.itau.journey;

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

    @Override
    public String toString() {
        return "RequestStartDTO{" +
                "bpmnInstance='" + bpmnInstance + '\'' +
                ", sync=" + sync +
                ", variables=" + variables +
                '}';
    }
}
