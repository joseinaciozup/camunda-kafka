package br.com.itau.apiservice3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api-service3")
public class ApiServiceController {

    @GetMapping
    public String execService(){
        return "Exec service api-service 3";
    }

}
