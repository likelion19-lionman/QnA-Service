package com.example.springqnaapp;

import com.example.springqnaapp.common.util.JwtTokenizer;
import com.example.springqnaapp.domain.RefreshToken;
import com.example.springqnaapp.domain.Role;
import com.example.springqnaapp.domain.RoleEnum;
import com.example.springqnaapp.domain.User;
import com.example.springqnaapp.repository.RefreshTokenRepository;
import com.example.springqnaapp.repository.RoleRepository;
import com.example.springqnaapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringQnaAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringQnaAppApplication.class, args);
	}

	@Bean
	@Profile("dev")
	public CommandLineRunner clr(RoleRepository roleRepository,
	                             UserRepository userRepository,
	                             PasswordEncoder passwordEncoder,
	                             JwtTokenizer jwtTokenizer,
	                             RefreshTokenRepository refreshTokenRepository) {

		return args -> {
			var adminRole = roleRepository.save(new Role(RoleEnum.ROLE_ADMIN));
			roleRepository.save(new Role(RoleEnum.ROLE_USER));

			var adminUser = userRepository.save(new User(
					"admin", "admin@example.com",
					passwordEncoder.encode("1234abc!"),
					adminRole));

			var accessToken = jwtTokenizer.createAccessToken(adminUser.getUsername(),
			                                                 adminUser.getEmail(),
			                                                 adminUser.getStringRoles());
			var refreshToken = jwtTokenizer.createRefreshToken(adminUser.getUsername(),
			                                                   adminUser.getEmail(),
			                                                   adminUser.getStringRoles());

			refreshTokenRepository.save(new RefreshToken(adminUser.getId(), refreshToken));
		};
	}
}
