package br.com.itau.journey.task;

import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ConsumerControllerTask implements JavaDelegate {

    private static Logger logger = LoggerFactory.getLogger(ConsumerControllerTask.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        try {
            ResponseEntity<String> forEntity = restTemplate.getForEntity(
                    "localhost:8095/worker/1", String.class);
            logger.info("Execution return {}", forEntity.getStatusCode());
        } catch (Exception e) {
            logger.info("Execution return {}", "ERROR");
        }
    }
}
