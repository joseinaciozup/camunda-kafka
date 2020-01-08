package br.itau.journey.feign;

import br.itau.journey.feign.dto.CompleteTaskRequest;
import br.itau.journey.feign.dto.FetchAndLockRequest;
import br.itau.journey.feign.dto.FetchAndLockResponse;
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

}
