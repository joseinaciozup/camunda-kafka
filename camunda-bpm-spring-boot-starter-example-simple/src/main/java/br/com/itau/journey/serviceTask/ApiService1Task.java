package br.com.itau.journey.serviceTask;

import java.util.Random;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;


@Component
public class ApiService1Task implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response
                    = restTemplate.getForEntity("http://localhost:8087/api-service1", String.class);
        }catch (HttpStatusCodeException exception){
            throw new RuntimeException("Ocorreu um erro codigo: "+exception.getStatusCode()+" mensagem: "+exception.getResponseBodyAsString());
        }
        Integer valorVariable = new Random().nextInt(3);
        delegateExecution.setVariable("valorVariable", valorVariable);

    }
}

