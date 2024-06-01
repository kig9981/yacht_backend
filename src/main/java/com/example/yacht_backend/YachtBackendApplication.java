package com.example.yacht_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class YachtBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(YachtBackendApplication.class, args);
	}

}
