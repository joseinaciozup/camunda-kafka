package br.com.itau.journey.service;

import br.com.itau.journey.camunda.rest.feign.dto.FetchAndLockResponse;
import org.json.JSONException;
import org.springframework.scheduling.annotation.Async;

public interface WorkerService {

    @Async("workerPollingAsyncThreads")
    void executionTask(FetchAndLockResponse fetchAndLockResponse, String workingPolling, String workerId) throws JSONException;
}
