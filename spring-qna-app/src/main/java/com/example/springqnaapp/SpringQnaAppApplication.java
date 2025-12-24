package com.example.springqnaapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringQnaAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringQnaAppApplication.class, args);
	}

}
