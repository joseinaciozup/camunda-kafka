package br.com.itau.journey.feign;

import br.com.itau.journey.camunda.rest.feign.CamundaExternalTaskApi;
import feign.Logger;
import feign.slf4j.Slf4jLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfiguration {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Logger logger() {
        return new Slf4jLogger(CamundaExternalTaskApi.class);
    }
}
