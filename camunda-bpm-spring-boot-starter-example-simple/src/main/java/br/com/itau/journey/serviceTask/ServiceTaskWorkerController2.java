package br.com.itau.journey.serviceTask;

import br.com.itau.journey.dto.RequestControllerStartDTO;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Component
public class ServiceTaskWorkerController2 implements JavaDelegate {

    private final String url = "http://localhost:8088/worker-controller-2";
    private final String A = "A";
    private final String STEP = "step";
    private final String DIRECTION_A = "directionA";
    private final String DIRECTION_B = "directionB";
    private final String TIME = "time2";

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        ResponseEntity<RequestControllerStartDTO> response;
        try {
            RestTemplate restTemplate = new RestTemplate();
            String direction = A.equals(delegateExecution.getVariable(STEP)) ? delegateExecution.getVariable(DIRECTION_A).toString()
                    : delegateExecution.getVariable(DIRECTION_B).toString();
            String time = delegateExecution.getVariable(TIME).toString();

            response = restTemplate.postForEntity(url,
                    RequestControllerStartDTO.builder().time(time).direction(direction).build(),
                    RequestControllerStartDTO.class);

        } catch (HttpStatusCodeException exception) {
            throw new RuntimeException("Ocorreu um erro codigo: " + exception.getStatusCode() + " mensagem: " + exception.getResponseBodyAsString());
        }
        delegateExecution.setVariable("valueDirection", response.getBody().getDirection());
    }
}

