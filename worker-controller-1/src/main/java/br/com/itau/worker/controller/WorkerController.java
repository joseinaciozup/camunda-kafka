package br.com.itau.worker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.itau.journey.camunda.rest.feign.dto.RequestControllerStartDTO;

@RestController
@RequestMapping("worker-controller-1")
public class WorkerController {

    @PostMapping
    public ResponseEntity<?> execService(@RequestBody RequestControllerStartDTO request) throws InterruptedException {

        if (Integer.parseInt(request.getTime()) > 0){
            Thread.sleep(Long.parseLong(request.getTime()));
        }else
            return new ResponseEntity("Ocorreu um erro no work-controller-1", HttpStatus.INTERNAL_SERVER_ERROR);


        return ResponseEntity.ok(request);
    }
}
