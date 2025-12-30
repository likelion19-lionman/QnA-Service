package com.example.springqnaapp.config;

import com.example.springqnaapp.domain.Role;
import com.example.springqnaapp.domain.RoleEnum;
import com.example.springqnaapp.repository.RoleRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

@Configuration
public class AppConfig {
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

    @Bean
    public Role defaultRole(RoleRepository roleRepository) {
        Optional<Role> role = roleRepository.findByRole(RoleEnum.ROLE_USER);
        return role.orElseGet(() -> roleRepository.save(new Role(RoleEnum.ROLE_USER)));
    }

    @Bean
    public Role adminRole(RoleRepository roleRepository) {
        Optional<Role> role = roleRepository.findByRole(RoleEnum.ROLE_ADMIN);
        return role.orElseGet(() -> roleRepository.save(new Role(RoleEnum.ROLE_ADMIN)));
    }
}