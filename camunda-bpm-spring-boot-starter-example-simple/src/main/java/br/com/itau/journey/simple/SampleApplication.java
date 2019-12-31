package br.com.itau.journey.simple;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.camunda.bpm.spring.boot.starter.property.CamundaBpmProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@Slf4j
@EnableFeignClients(basePackages ="br.com.itau.jorney")
public class SampleApplication implements CommandLineRunner {

    @Autowired
    private CamundaBpmProperties camundaBpmProperties;

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        log.info("CommandLineRunner#run() - {}", ToStringBuilder.reflectionToString(camundaBpmProperties.getGenerateUniqueProcessApplicationName(), ToStringStyle.SHORT_PREFIX_STYLE));
    }
}
