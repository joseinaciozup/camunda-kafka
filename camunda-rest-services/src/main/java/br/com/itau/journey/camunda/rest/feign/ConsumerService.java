package br.com.itau.journey.camunda.rest.feign;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.itau.journey.camunda.rest.feign.dto.CompleteTaskRequest;
import br.com.itau.journey.camunda.rest.feign.dto.FailureRequest;
import br.com.itau.journey.camunda.rest.feign.dto.FetchAndLockRequest;
import br.com.itau.journey.camunda.rest.feign.dto.FetchAndLockResponse;
import br.com.itau.journey.camunda.rest.feign.dto.TopicRequest;

@Service
public class ConsumerService {

    private static final Logger log = LoggerFactory.getLogger(ConsumerService.class);

    private CamundaExternalTaskApi camundaExternalTaskApi;

    @Autowired
    public ConsumerService(CamundaExternalTaskApi camundaExternalTaskApi) {
        this.camundaExternalTaskApi = camundaExternalTaskApi;
    }

    public List<FetchAndLockResponse> getTasksByTopic(String camundaTopic, String workerId, Integer maxTasks) {
        log.info("[Camunda Service Rest] - GETING TASK BY TOPIC [{}], WORKER ID [{}]", camundaTopic, workerId);
        List<FetchAndLockResponse> fetchAndLockResponses = camundaExternalTaskApi.fetchAndLock(FetchAndLockRequest.builder()
                .workerId(workerId)
                .maxTasks(maxTasks)
                .usePriority(true)
                .topics(Arrays.asList(TopicRequest.builder()
                        .topicName(camundaTopic)
                        .lockDuration(60000L)
                        .build()))
                .build());
        log.info("[Camunda Service Rest] - COUNT LIST TASK BY COUNT [{}], TOPIC [{}], WORKER ID [{}]",  fetchAndLockResponses.size(), camundaTopic, workerId);
        return fetchAndLockResponses;
    }

    public void completeTask(FetchAndLockResponse fetchAndLockResponse, String workerId) {
        log.info("[Camunda Service Rest] - COMPLETING [{}] EXTERNAL TASK", fetchAndLockResponse.getId());
        camundaExternalTaskApi.complete(fetchAndLockResponse.getId(), CompleteTaskRequest.builder()
                .workerId(workerId)
                .variables(getVariables(fetchAndLockResponse.getVariables()))
                .build());
        log.info("[Camunda Service Rest] - FINISHED COMPLETE [{}] EXTERNAL TASK", fetchAndLockResponse.getId());
    }

    public void handlerFailure(FetchAndLockResponse fetchAndLockResponse, String error) {
        log.info("[Camunda Service Rest] - SEND FAILURE [{}] ", fetchAndLockResponse.getWorkerId());
        camundaExternalTaskApi.handlerFailure(fetchAndLockResponse.getId(),
                FailureRequest.builder()
                .workerId(fetchAndLockResponse.getWorkerId())
                .errorMessage(error)
                .retries(0L)
                .retryTimeout(60000L)
                .build());
        log.info("[Camunda Service Rest] - FINISHED SEND FAILURE [{}] ", fetchAndLockResponse.getWorkerId());
    }

    private Map<String, CompleteTaskRequest.Value> getVariables(Map mapVariable) {
        Map<String, CompleteTaskRequest.Value> variables = new HashMap<>();
        mapVariable.forEach((key, value) -> {
            variables.put(key.toString(), CompleteTaskRequest.Value.builder().value(value.toString()).build());
        });
        return variables;
    }

}
