package br.com.itau.apiservice1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api-service1")
public class ApiServiceController {

    @GetMapping
    public String execService(){
        return "exec api service 1";
    }
}
