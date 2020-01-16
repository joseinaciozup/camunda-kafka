package br.itau.journey.worker;

import br.itau.journey.camunda.rest.feign.ConsumerService;
import br.itau.journey.camunda.rest.feign.dto.FetchAndLockResponse;
import br.itau.journey.constant.CamundaConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class WorkerStarter {

    private String ZERO = "0";

    @Value("${camunda.worker-topic}")
    private String WORKER_POLLING;

    @Value("${camunda.worker-id}")
    private String WORKER_ID;

    private String TIME = CamundaConstants.TIME3.getDescricao();

    @Autowired
    private ConsumerService consumerService;

    @Scheduled(fixedDelayString = "${camunda.worker-schedule}")
    @Async("workerPollingAsync")
    public void start() {
        log.info("[{}}] - Starting...", WORKER_POLLING);

        List<FetchAndLockResponse> tasks = findPendentTasks();
        tasks.stream().forEach(t -> {
            try {
                executionTask(t);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        log.info("[{}}] - End...", WORKER_POLLING);
    }

    private List<FetchAndLockResponse> findPendentTasks() {
        return consumerService.getTasksByTopic(WORKER_POLLING, WORKER_ID);
    }

    private String executionTask(FetchAndLockResponse fetchAndLockResponse) throws JSONException {
        log.info("[{}}] - Execution Task Starting...", WORKER_POLLING);
        if (!isIncident(fetchAndLockResponse.getVariables())) {
            try {
                HashMap<String, Object> variables = new HashMap<>();
                final String time = getVariable(fetchAndLockResponse.getVariables(), TIME);
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
            log.info("[{}}] - Execution Task End...", WORKER_POLLING);
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
        return getVariable(variables, TIME).equals(ZERO);
    }

    private void handlerFailure(FetchAndLockResponse fetchAndLockResponse) {
        log.info("[{}}] - Execution Handler Failure - Starting...", WORKER_POLLING);
        String message = "Ocorreu um erro na execução do serviço - [{" + WORKER_POLLING + "}]";
        log.info(message);
        consumerService.handlerFailure(fetchAndLockResponse, message);
        log.info("[{}}] - Execution Handler Failure - End...", WORKER_POLLING);
    }

}
