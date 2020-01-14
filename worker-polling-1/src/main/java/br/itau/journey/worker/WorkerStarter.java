package br.itau.journey.worker;

import br.itau.journey.camunda.rest.feign.ConsumerService;
import br.itau.journey.camunda.rest.feign.dto.FetchAndLockResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class WorkerStarter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerStarter.class);

    @Value("${camunda.worker-topic}")
    private String WORKER_POLLING;

    @Value("${camunda.worker-id}")
    private String WORKER_ID;

    @Autowired
    private ConsumerService consumerService;


    @Scheduled(fixedDelay = 5000)
    public void start() {
        LOGGER.info("Starting...");

        List<FetchAndLockResponse> tasks = findPendentTasks();
        tasks.stream().forEach(t -> executionTask(t));

        LOGGER.info("End...");
    }

    private List<FetchAndLockResponse> findPendentTasks() {
        return consumerService.getTasksByTopic(WORKER_POLLING, WORKER_ID);
    }

    private String executionTask(FetchAndLockResponse fetchAndLockResponse) {
        Integer valorExecution = new Random().nextInt(6);
//        if (valorExecution > 0) {
        if (true) {
            try {
                Thread.sleep((valorExecution * 1000));
                Map<String, Integer> variables = new HashMap<>();
                variables.put("valorVariable", valorExecution);
                fetchAndLockResponse.setVariables(variables);

                consumerService.completeTask(fetchAndLockResponse, WORKER_ID);
            } catch (InterruptedException e) {
                handlerFailure(fetchAndLockResponse);
                e.printStackTrace();
            }
            return "Executando a API [{" + WORKER_ID + "}]";
        } else {
            handlerFailure(fetchAndLockResponse);
            return StringUtils.EMPTY;
        }
    }

    private void handlerFailure(FetchAndLockResponse fetchAndLockResponse) {
        String message = "Ocorreu um erro na execução do serviço - [{" + WORKER_POLLING + "}]";
        LOGGER.info(message);
        consumerService.handlerFailure(fetchAndLockResponse, message);
    }

}
