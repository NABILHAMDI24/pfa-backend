package com.example.idscanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.example.idscanner.model") // Scan for entities
@EnableJpaRepositories(basePackages = "com.example.idscanner.repository") // Scan for repositories
public class IdscannerApplication {
	public static void main(String[] args) {
		SpringApplication.run(IdscannerApplication.class, args);
	}
}