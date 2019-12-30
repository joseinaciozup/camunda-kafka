package br.com.itau.journey.controller;

import java.math.BigDecimal;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("create-jorney")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CreateJorneyControlle {

    private final RuntimeService runtimeService;


    @PostMapping
    public void createJorney(){

        createProcess();
    }

    private ProcessInstance createProcess() {
        return runtimeService.startProcessInstanceByKey("exemplo1",
                Variables.putValue("valorVariable1","t1" ));
    }
}
