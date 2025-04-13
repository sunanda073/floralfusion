package com.floralfusion.floralfusion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FloralfusionApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(FloralfusionApplication.class, args);
	} 

}
