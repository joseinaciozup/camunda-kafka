package br.com.itau.journey.feign;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@RefreshScope
@FeignClient(
        name = "api-service2",
        url = "localhost:8088"
)
public interface ApiService2Feign {

    @GetMapping(
            path = {"/api-service2"},
            consumes = {"application/json"}
    )
    void execService();
}
