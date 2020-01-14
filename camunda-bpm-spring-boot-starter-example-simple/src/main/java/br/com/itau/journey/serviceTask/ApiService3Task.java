package br.com.itau.journey.serviceTask;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;


@Component
public class ApiService3Task  implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response
                    = restTemplate.getForEntity("http://localhost:8089/api-service3", String.class);
        } catch (HttpStatusCodeException exception) {
            throw new RuntimeException("Ocorreu um erro codigo: " + exception.getStatusCode() + " mensagem: " + exception.getResponseBodyAsString());
        }
    }
}
