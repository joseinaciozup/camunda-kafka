package br.com.itau.worker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.itau.journey.camunda.rest.feign.dto.RequestControllerStartDTO;

@RestController
@RequestMapping("worker-controller-2")
@Slf4j
public class WorkerController {

    @Value("${camunda.worker-name}")
    private String WORKER_NAME;

    @PostMapping
    public ResponseEntity<?> execService(@RequestBody RequestControllerStartDTO request) throws InterruptedException {
        log.info("[{}}] - Starting...", WORKER_NAME);
        if (Integer.parseInt(request.getTime()) > 0){
            Thread.sleep(Long.parseLong(request.getTime()));
        } else {
            return new ResponseEntity("Ocorreu um erro no [{" + WORKER_NAME + "}]", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("[{}}] - End...", WORKER_NAME);
        return ResponseEntity.ok(request);
    }
}
