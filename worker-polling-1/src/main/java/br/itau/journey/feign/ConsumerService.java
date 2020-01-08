package br.itau.journey.feign;

import br.itau.journey.feign.dto.CompleteTaskRequest;
import br.itau.journey.feign.dto.FetchAndLockRequest;
import br.itau.journey.feign.dto.FetchAndLockResponse;
import br.itau.journey.feign.dto.TopicRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ConsumerService {

    public static final String TEST_EXTERNAL_WORKER_ID = "testExternalWorkerId";
    private CamundaExternalTaskApi camundaExternalTaskApi;

    @Autowired
    public ConsumerService(CamundaExternalTaskApi camundaExternalTaskApi) {
        this.camundaExternalTaskApi = camundaExternalTaskApi;
    }

    public List<FetchAndLockResponse> getTasksByTopic(String camundaTopic) {

        List<FetchAndLockResponse> fetchAndLockResponses = camundaExternalTaskApi.fetchAndLock(FetchAndLockRequest.builder()
                .workerId(TEST_EXTERNAL_WORKER_ID)
                .maxTasks(1)
                .usePriority(true)
                .topics(Arrays.asList(TopicRequest.builder()
                        .topicName(camundaTopic)
                        .lockDuration(60000L)
                        .build()))
                .build());

        return fetchAndLockResponses;
    }

    public boolean completeTask(FetchAndLockResponse fetchAndLockResponse) {
        log.info("COMPLETING[{}] EXTERNAL TASK", fetchAndLockResponse.getId());
        camundaExternalTaskApi.complete(fetchAndLockResponse.getId(), CompleteTaskRequest.builder()
                .workerId(TEST_EXTERNAL_WORKER_ID)
                .build());
        return true;
    }

}
