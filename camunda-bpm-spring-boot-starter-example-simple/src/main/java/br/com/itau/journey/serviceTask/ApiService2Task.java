package br.com.itau.journey.serviceTask;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class ApiService2Task implements JavaDelegate {

    //private ApiService2Feign apiService2Feign;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        //apiService2Feign.execService();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response
                = restTemplate.getForEntity("http://localhost:8088/api-service2", String.class);

    }
}
