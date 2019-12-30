package br.com.itau.apiservice2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api-service2")
public class ApiServiceController {

    @GetMapping
    public String exeService(){
        return "exec service api-service 2";
    }
}
