package br.com.itau.journey.serviceTask;

import java.util.Random;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class ApiService1Task implements JavaDelegate {

    //private ApiService1Feign apiService1Feign;

    //@Autowired
    //public ApiService1Task(ApiService1Feign apiService1Feign){
    //    this.apiService1Feign = apiService1Feign;
    //}


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

       // apiService1Feign.execService();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response
                = restTemplate.getForEntity("http://localhost:8087/api-service1", String.class);

        Integer valorVariable = new Random().nextInt(3);
        delegateExecution.setVariable("valorVariable", valorVariable);

    }
}

