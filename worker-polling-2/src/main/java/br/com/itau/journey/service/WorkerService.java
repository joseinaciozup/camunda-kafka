package br.com.itau.journey.service;

import br.com.itau.journey.camunda.rest.feign.ConsumerService;
import br.com.itau.journey.camunda.rest.feign.dto.FetchAndLockResponse;
import br.com.itau.journey.constant.CamundaConstants;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WorkerService {

    private static final Logger log = LoggerFactory.getLogger(ConsumerService.class);

    private String ZERO = "0";

    @Value("${camunda.worker-topic}")
    private String WORKER_POLLING;

    @Value("${camunda.worker-time}")
    private String WORKER_TIME;

    @Autowired
    private ConsumerService consumerService;

    public void executionTask(FetchAndLockResponse fetchAndLockResponse, String workingPolling, String workerId) throws JSONException {
        log.info("[{}}] - Execution Task Starting...", workingPolling);
        if (!isIncident(fetchAndLockResponse.getVariables())) {
            try {
                HashMap<String, Object> variables = new HashMap<>();
                final String time = getVariable(fetchAndLockResponse.getVariables(), WORKER_TIME);
                final String step = getVariable(fetchAndLockResponse.getVariables(), CamundaConstants.STEP.getDescricao());

                final String direction = getDirection(fetchAndLockResponse, step);

                variables.put("valueDirection", Integer.valueOf(direction));
                fetchAndLockResponse.setVariables(variables);

                Thread.sleep(Long.parseLong(time));

                consumerService.completeTask(fetchAndLockResponse, workerId);
            } catch (InterruptedException | JSONException e) {
                handlerFailure(fetchAndLockResponse);
                e.printStackTrace();
            }
            log.info("[{}}] - Execution Task End...", workingPolling);
        } else {
            handlerFailure(fetchAndLockResponse);
        }
    }

    private boolean isIncident(Map variables) throws JSONException {
        return getVariable(variables, WORKER_TIME).equals(ZERO);
    }

    private String getDirection(FetchAndLockResponse fetchAndLockResponse, String step) throws JSONException {
        return "A".equals(step) ? getVariable(fetchAndLockResponse.getVariables(), CamundaConstants.DIRECTION_A.getDescricao()):
                getVariable(fetchAndLockResponse.getVariables(), CamundaConstants.DIRECTION_B.getDescricao());
    }

    private void handlerFailure(FetchAndLockResponse fetchAndLockResponse) {
        log.info("[{}}] - Execution Handler Failure - Starting...", WORKER_POLLING);
        String message = "Ocorreu um erro na execução do serviço - [{" + WORKER_POLLING + "}]";
        log.info(message);
        consumerService.handlerFailure(fetchAndLockResponse, message);
        log.info("[{}}] - Execution Handler Failure - End...", WORKER_POLLING);
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

}
