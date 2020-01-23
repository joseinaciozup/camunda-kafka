package br.com.itau.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class WorkerControllerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkerControllerApplication.class, args);
	}

}
