package br.com.itau.journey.serviceTask;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class ApiService3Task  implements JavaDelegate {

    //private ApiService3Feign apiService3Feign;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        //apiService3Feign.execService();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response
                = restTemplate.getForEntity("http://localhost:8089/api-service3", String.class);
    }
}
