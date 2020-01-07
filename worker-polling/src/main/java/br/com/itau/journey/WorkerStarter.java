package br.com.itau.journey;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkerStarter {

    private static Logger logger = LoggerFactory.getLogger(WorkerStarter.class);

    @Autowired
    private TaskService taskService;

    private static String processInstanceId = "workerPolling";

    @Async
    @Scheduled(fixedDelay = 10000)
    public void start() throws InterruptedException {
        logger.info("Start scheduled...");

        List<Task> list = findPendentTasks();

        list.stream().forEach(t -> {
            try {
                Thread.sleep(2000); // END POINT SIGLA PRODUTO BANCO
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        logger.info("End scheduled...");
    }

    private List<Task> findPendentTasks() {
        return taskService.createTaskQuery().processInstanceId(processInstanceId).list();
    }
}
