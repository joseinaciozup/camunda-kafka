package br.com.itau.journey.controller;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("start")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StartProcessInstanceController {

    private final RuntimeService runtimeService;

    @PostMapping
    public String start(@RequestBody String bpmnInstance){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(bpmnInstance);
        return processInstance.getProcessInstanceId();
    }
}
