package br.com.itau.journey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariablesStartDTO {

    private String timeService1;
    private String timeService2;
    private String timeService3;
    private String directionA;
    private String directionB;
}
