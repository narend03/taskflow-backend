package com.taskflow.taskflow_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
@SpringBootApplication(scanBasePackages = "com.taskflow.taskflow_backend")
public class TaskflowBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskflowBackendApplication.class, args);
	}

}
