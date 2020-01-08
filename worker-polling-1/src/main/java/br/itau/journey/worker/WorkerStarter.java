package br.itau.journey.worker;

import br.itau.journey.feign.ConsumerService;
import br.itau.journey.feign.dto.FetchAndLockResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class WorkerStarter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerStarter.class);
    private static final String WORKER_POLLING = "WorkingPolling";

    @Autowired
    private ConsumerService consumerService;


    @Scheduled(fixedDelay = 30000)
    public void start()  {
        LOGGER.info("Starting...");

        List<FetchAndLockResponse> tasks = findPendentTasks();
        tasks.stream().forEach(t -> executionTask(t));

        LOGGER.info("End...");
    }

    private List<FetchAndLockResponse> findPendentTasks() {
        return consumerService.getTasksByTopic(WORKER_POLLING);
    }

    private String executionTask(FetchAndLockResponse fetchAndLockResponse) {
        Integer valorExecution = new Random().nextInt(6);
        if (valorExecution > 0){
            try {
                Thread.sleep((valorExecution * 1000));
                Map<String, Integer> variables = new HashMap<>();
                variables.put("valorVariable", valorExecution);
                fetchAndLockResponse.setVariables(variables);

                consumerService.completeTask(fetchAndLockResponse);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Executando a API service 3";
        } else {
            throw new RuntimeException("Ocorreu um erro na execução do serviço API service 3");
        }
    }

}
