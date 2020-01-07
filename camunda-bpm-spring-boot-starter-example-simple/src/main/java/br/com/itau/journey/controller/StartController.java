package br.com.itau.journey.controller;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.variable.Variables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/start")
public class StartController {

    private static Logger logger = LoggerFactory.getLogger(StartController.class);

    @Autowired
    private RuntimeService runtimeService;

    @PostMapping("/{isController}")
    public void createOrder(@PathVariable boolean isController) {
        logger.info("Start Process");
        runtimeService.startProcessInstanceByKey("ProcessCallActivityError", Variables.putValue("isController", isController));
    }
}
