package br.com.itau.journey.controller;

import br.com.itau.journey.CorrelationId;
import br.com.itau.journey.Message;
import br.com.itau.journey.dispatcher.KafkaDispatcher;
import br.com.itau.journey.dto.RequestStartDTO;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("start")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StartProcessInstanceController {

    private final RuntimeService runtimeService;

    private final ProcessEngine processEngine;

    private Map<String, String> variables = new HashMap<>();

    private final KafkaDispatcher<String> dispatcher = new KafkaDispatcher<>();

    @PostMapping
    public String start(@RequestBody RequestStartDTO requestStart) throws InterruptedException, ExecutionException {
        UUID id = UUID.randomUUID();
        Message message = new Message(new CorrelationId("Start"), "Start");

        dispatcher.send("create.journey", id.toString(), message.getId().continueWith(StartProcessInstanceController.class.getSimpleName()), requestStart.toString());


        while {
            lendo no topico end.journey se a instancia id = 1234
        }

        return "Journey Creating!";
    }
}
