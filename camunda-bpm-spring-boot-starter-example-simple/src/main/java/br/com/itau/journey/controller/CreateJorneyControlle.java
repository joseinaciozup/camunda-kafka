package br.com.itau.journey.controller;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping("/{processInstanceId}/complete")
    public void createCompleteTask(@PathVariable(value = "processInstanceId") String processInstanceId){

        Map<String,Object> formParams = new HashMap<>();
        formParams.put("name","teste - nome");
        formParams.put("sobrenome","teste - sobrenome");

        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        processEngine.getFormService().submitTaskForm(task.getId(),formParams );
    }

    private ProcessInstance createProcess() {
        return runtimeService.startProcessInstanceByKey("exemplo1");
    }
}
