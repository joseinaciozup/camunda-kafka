package br.com.itau.worker.controller;

import br.itau.journey.camunda.rest.feign.dto.RequestControllerStartDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("worker-controller-1")
public class WorkerController {

    @PostMapping
    public ResponseEntity<?> execService(@RequestBody RequestControllerStartDTO request) throws InterruptedException {
        Thread.sleep(Long.parseLong(request.getTime()));
        return ResponseEntity.ok(request);
    }
}
