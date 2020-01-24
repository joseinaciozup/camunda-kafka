package br.com.itau.journey.controller;

import br.com.itau.journey.dto.RequestStartDTO;
import br.com.itau.journey.dto.VariablesStartDTO;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("start")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StartProcessInstanceController {

    private final RuntimeService runtimeService;

    private final ProcessEngine processEngine;

    private Map<String, String> variables = new HashMap<>();

    @PostMapping
    public String start(@RequestBody RequestStartDTO requestStart) throws InterruptedException {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(requestStart.getBpmnInstance(), getVariables(requestStart.getVariables()));

        if (requestStart.isSync()) {
           waitingProcessEnd(processInstance);
        }

        return processInstance.getProcessInstanceId();
    }

    private void waitingProcessEnd(ProcessInstance processInstance) throws InterruptedException {
        boolean x = true;
        while (x) {
            Thread.sleep(5000);
            x = processEngine
                    .getRuntimeService()
                    .createProcessInstanceQuery()
                    .processInstanceId(processInstance.getId())
                    .singleResult() != null;
        }
    }

    private Map<String, Object> getVariables(VariablesStartDTO variablesStartDTO) {
        Map<String, Object> variables = new HashMap<>();

        variables.put("time1", variablesStartDTO.getTimeService1());
        variables.put("time2", variablesStartDTO.getTimeService2());
        variables.put("time3", variablesStartDTO.getTimeService3());
        variables.put("directionA", variablesStartDTO.getDirectionA());
        variables.put("directionB", variablesStartDTO.getDirectionB());
        variables.put("valueDirection", 0);

        return variables;
    }
}
