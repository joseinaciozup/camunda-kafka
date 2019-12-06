package br.com.itau.journey.feign;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RefreshScope
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
