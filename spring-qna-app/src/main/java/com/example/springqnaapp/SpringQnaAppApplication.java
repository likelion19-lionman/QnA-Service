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

import java.util.List;

@SpringBootApplication
public class SpringQnaAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringQnaAppApplication.class, args);
	}

	@Profile("dev")
	@Bean
	public CommandLineRunner commandLineRunner(
			RoleRepository roleRepository,
			RefreshTokenRepository refreshTokenRepository,
			JwtTokenizer jwtTokenizer,
			UserRepository userRepository,
			PasswordEncoder passwordEncoder
			) {
		return (args) -> {
			roleRepository.saveAllAndFlush(List.of(
					new Role(RoleEnum.ROLE_USER),
					new Role(RoleEnum.ROLE_ADMIN)
			));
			var adminRole = roleRepository.findByRole(RoleEnum.ROLE_ADMIN).get();

			User admin = userRepository.save(
					new User("admin",
					         "admin@naver.com",
					         passwordEncoder.encode("admin123!"),
					         adminRole
					)
			);

			String adminAccessToken = jwtTokenizer.createAccessToken(admin.getUsername(), admin.getEmail(), admin.getStringRoles());
			String adminRefreshToken = jwtTokenizer.createRefreshToken(admin.getUsername(), admin.getEmail(), admin.getStringRoles());

			refreshTokenRepository.save(new RefreshToken(admin.getId(), adminRefreshToken));

			System.out.println("========== Admin =========");
			System.out.println("accessToken : " + adminAccessToken);
			System.out.println("refreshToken : " + adminRefreshToken);
			System.out.println("=========================");
		};
	}
}
