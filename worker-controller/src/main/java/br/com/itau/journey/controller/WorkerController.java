package br.com.itau.journey.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/worker")
public class WorkerController {

    private static Logger logger = LoggerFactory.getLogger(WorkerController.class);

    @GetMapping("/{id}")
    public String createOrder(@PathVariable int id) throws InterruptedException {
        logger.info("Consumer Controller ID {}", id);
        Thread.sleep(2000); // END POINT SIGLA PRODUTO BANCO
        return "OK";
    }
}
