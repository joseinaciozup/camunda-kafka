package br.com.itau.journey.controller;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("userTask")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserTaskController {

    private final TaskService taskService;

    private final FormService formService;

    @PostMapping("selecionaLoja/{processInstanceId}/complete")
    public void selecionaLoja(@PathVariable(value = "processInstanceId") String processInstanceId){

        Map<String,Object> formParams = new HashMap<>();
        formParams.put("name_loja","Extra");

        compleUserTask(processInstanceId, formParams);

        //return getVariablesProcess(processInstanceId);

    }



    @PostMapping("selecionaDados/{processInstanceId}/complete")
    public void selecionaDados(@PathVariable(value = "processInstanceId") String processInstanceId){

        Map<String,Object> formParams = new HashMap<>();
        formParams.put("name","teste - nome");
        formParams.put("sobrenome","teste - sobrenome");

        compleUserTask(processInstanceId, formParams);

        //return getVariablesProcess(processInstanceId);
    }

    @PostMapping("selecionaOferta/{processInstanceId}/complete")
    public void selecionaOferta(@PathVariable(value = "processInstanceId") String processInstanceId){

        Map<String,Object> formParams = new HashMap<>();
        formParams.put("ofeta_id","cartao-de-credito");
        compleUserTask(processInstanceId, formParams);

        //return getVariablesProcess(processInstanceId);
    }

    @PostMapping("finalizaTermo/{processInstanceId}/complete")
    public String finalizaTermo(@PathVariable(value = "processInstanceId") String processInstanceId){

        Map<String,Object> formParams = new HashMap<>();

        compleUserTask(processInstanceId, formParams);

        return "Processo Finalizado com Sucesso!!!";

    }

    private Map<String, Object> getVariablesProcess(
            @PathVariable("processInstanceId") String processInstanceId) {
        Task task = taskService.createTaskQuery().executionId(processInstanceId).singleResult();
        return taskService.getVariables(task.getId());

    }

    private void compleUserTask( String processInstanceId, Map<String, Object> formParams) {
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        formService.submitTaskForm(task.getId(), formParams);
    }

}
