package com.example.batchOrq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class BatchOrchestrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchOrchestrationApplication.class, args);
	}


}
