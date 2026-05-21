package com.camtech.bi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BiServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(BiServiceApplication.class, args);
	}
}
