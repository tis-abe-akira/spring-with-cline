package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@OpenAPIDefinition(
	info = @io.swagger.v3.oas.annotations.info.Info(
		title = "SpringWithCline",
		version = "1.0.0"
	)
)
@SpringBootApplication
public class SpringWithClineApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWithClineApplication.class, args);
	}

}
