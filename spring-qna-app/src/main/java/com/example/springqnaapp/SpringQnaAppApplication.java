package com.example.springqnaapp;

import com.example.springqnaapp.domain.Role;
import com.example.springqnaapp.domain.RoleEnum;
import com.example.springqnaapp.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.List;

@SpringBootApplication
public class SpringQnaAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringQnaAppApplication.class, args);
	}

	@Profile("dev")
	@Bean
	public CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
		return (args) -> roleRepository.saveAllAndFlush(List.of(
				new Role(RoleEnum.ROLE_USER),
				new Role(RoleEnum.ROLE_ADMIN))
		);
	}
}
