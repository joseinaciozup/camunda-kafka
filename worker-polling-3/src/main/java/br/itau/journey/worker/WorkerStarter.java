package br.itau.journey.worker;

import br.itau.journey.camunda.rest.feign.ConsumerService;
import br.itau.journey.camunda.rest.feign.dto.CompleteTaskRequest;
import br.itau.journey.camunda.rest.feign.dto.FetchAndLockResponse;
import br.itau.journey.constant.CamundaConstants;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class WorkerStarter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerStarter.class);
    private String ZERO = "0";

    @Value("${camunda.worker-topic}")
    private String WORKER_POLLING;

    @Value("${camunda.worker-id}")
    private String WORKER_ID;

    @Autowired
    private ConsumerService consumerService;

    @Scheduled(fixedDelayString = "${camunda.worker-schedule}")
    @Async("workerPollingAsync")
    public void start() {
        LOGGER.info("Starting...");

        List<FetchAndLockResponse> tasks = findPendentTasks();
        tasks.stream().forEach(t -> {
            try {
                executionTask(t);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        LOGGER.info("End...");
    }

    private List<FetchAndLockResponse> findPendentTasks() {
        return consumerService.getTasksByTopic(WORKER_POLLING, WORKER_ID);
    }

    private String executionTask(FetchAndLockResponse fetchAndLockResponse) throws JSONException {
        if (!isIncident(fetchAndLockResponse.getVariables())) {
            try {
                HashMap<String, Object> variables = new HashMap<>();
                final String time = getVariable(fetchAndLockResponse.getVariables(), CamundaConstants.TIME3.getDescricao());
                final String step = getVariable(fetchAndLockResponse.getVariables(), CamundaConstants.STEP.getDescricao());

                final String direction = getDirection(fetchAndLockResponse, step);

                variables.put("valueDirection", Integer.valueOf(direction));
                fetchAndLockResponse.setVariables(variables);

                Thread.sleep(Long.parseLong(time));

                consumerService.completeTask(fetchAndLockResponse, WORKER_ID);
            } catch (InterruptedException | JSONException e) {
                handlerFailure(fetchAndLockResponse);
                e.printStackTrace();
            }
            return "Executando a API [{" + WORKER_ID + "}]";
        } else {
            handlerFailure(fetchAndLockResponse);
            return StringUtils.EMPTY;
        }
    }

    private String getDirection(FetchAndLockResponse fetchAndLockResponse, String step) throws JSONException {
        return "A".equals(step) ? getVariable(fetchAndLockResponse.getVariables(), CamundaConstants.DIRECTION_A.getDescricao()):
                                  getVariable(fetchAndLockResponse.getVariables(), CamundaConstants.DIRECTION_B.getDescricao());
    }

    private String getVariable(Map<String, Object> variables, String field) throws JSONException {
        String result = StringUtils.EMPTY;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            if (field.equals(entry.getKey())) {
                JSONObject json = new JSONObject(entry.getValue().toString());
                result = json.get("value").toString();
            }
        }
        return result;
    }

    private boolean isIncident(Map variables) throws JSONException {
        return getVariable(variables, CamundaConstants.TIME1.getDescricao()).equals(ZERO);
    }

    private void handlerFailure(FetchAndLockResponse fetchAndLockResponse) {
        String message = "Ocorreu um erro na execução do serviço - [{" + WORKER_POLLING + "}]";
        LOGGER.info(message);
        consumerService.handlerFailure(fetchAndLockResponse, message);
    }

}
