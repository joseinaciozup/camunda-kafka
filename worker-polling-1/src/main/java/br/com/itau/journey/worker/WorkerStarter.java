package br.com.itau.journey.worker;

import br.com.itau.journey.camunda.rest.feign.ConsumerService;
import br.com.itau.journey.camunda.rest.feign.dto.FetchAndLockResponse;
import br.com.itau.journey.service.WorkerService;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkerStarter {

    private static final Logger log = LoggerFactory.getLogger(ConsumerService.class);

    @Value("${camunda.worker-topic}")
    private String WORKER_TOPIC;

    @Value("${camunda.worker-id}")
    private String WORKER_ID;

    @Value("${camunda.worker-max-taks}")
    private Integer WORKER_MAX_TASKS;

    @Value("${camunda.worker-multiple-topics}")
    private List<String> TOPIC_LIST;

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private WorkerService workerService;

    @Scheduled(fixedDelayString = "${camunda.worker-schedule}")
    @Async("workerPollingAsyncStarter")
    public void startStarter() {
        TOPIC_LIST.stream().forEach(topic ->
            {
                log.info("[{}}] - Starting...", topic);

                List<FetchAndLockResponse> tasks = findPendentTasks(topic, WORKER_ID, WORKER_MAX_TASKS);
                tasks.stream().forEach(t -> {
                    startThread(t, topic, WORKER_ID);
                });
                log.info("[{}}] - End...", topic);
            }
        );
    }

    private void startThread(FetchAndLockResponse task, String workerTopic, String workerId) {
        new Thread(() -> {
            try {
                workerService.executionTask(task, workerTopic, workerId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private List<FetchAndLockResponse> findPendentTasks(String workerPolling, String workerId, Integer maxTasks) {
        return consumerService.getTasksByTopic(workerPolling, workerId, maxTasks);
    }

}
