package br.com.itau.journey.controller;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
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

    private final ProcessEngine processEngine;

    private final TaskService taskService;


    @PostMapping
    public String createJorney(){

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("exemplo1");
        return processInstance.getProcessInstanceId();
    }

}
