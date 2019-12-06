package org.camunda.bpm.spring.boot.example.simple;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.camunda.bpm.spring.boot.starter.property.CamundaBpmProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SampleApplication implements CommandLineRunner {

    @Autowired
    private CamundaBpmProperties camundaBpmProperties;

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        log.info("CommandLineRunner#run() - {}", ToStringBuilder.reflectionToString(camundaBpmProperties.getApplication(), ToStringStyle.SHORT_PREFIX_STYLE));
    }
}