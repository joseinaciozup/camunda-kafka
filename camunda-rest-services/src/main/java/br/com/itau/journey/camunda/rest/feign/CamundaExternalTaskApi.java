package br.com.itau.journey.camunda.rest.feign;

import br.com.itau.journey.camunda.rest.feign.dto.FetchAndLockResponse;
import br.com.itau.journey.camunda.rest.feign.dto.CompleteTaskRequest;
import br.com.itau.journey.camunda.rest.feign.dto.FailureRequest;
import br.com.itau.journey.camunda.rest.feign.dto.FetchAndLockRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(
        name = "externalTask",
        url = "${camunda.endpoint}"
)
public interface CamundaExternalTaskApi {

    @PostMapping(
            path = {"/external-task/{externalTaskId}/complete"},
            consumes = {"application/json"}
    )
    void complete(@PathVariable(name = "externalTaskId") String catalogId, CompleteTaskRequest authenticationRequest);

    @PostMapping(
            path = {"/external-task/fetchAndLock"},
            consumes = {"application/json"}
    )
    List<FetchAndLockResponse> fetchAndLock(FetchAndLockRequest authenticationRequest);

    @PostMapping(
            path = {"/external-task/{externalTaskId}/failure"},
            consumes = {"application/json"}
    )
    void handlerFailure(@PathVariable(name = "externalTaskId") String externalTaskId, FailureRequest failureRequest);

}
