package br.com.itau.journey.feign;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@RefreshScope
@FeignClient(
        name = "api-service3",
        url = "localhost:8089"
)
public interface ApiService3Feign {

    @GetMapping(
            path = {"/api-service3"},
            consumes = {"application/json"}
    )
    void execService();
}
