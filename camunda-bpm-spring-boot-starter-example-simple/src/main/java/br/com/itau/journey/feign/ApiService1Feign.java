package br.com.itau.journey.feign;


import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@RefreshScope
@FeignClient(
        name = "api-service1",
        url = "localhost:8087"
)
public interface ApiService1Feign {

    @GetMapping(
            path = {"/api-service1"},
            consumes = {"application/json"}
    )
    void execService();

}
