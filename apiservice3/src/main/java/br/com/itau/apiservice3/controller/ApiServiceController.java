package br.com.itau.apiservice3.controller;

import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api-service3")
public class ApiServiceController {

    @GetMapping
    public String execService() throws InterruptedException {

        Integer valorExecution = new Random().nextInt(6);
        if (valorExecution > 0){
            Thread.sleep((valorExecution * 1000));

            return "Executando a API service 3";
        }else
            throw new RuntimeException("Ocorreu um erro na execução do serviço API service 3");
    }

}
