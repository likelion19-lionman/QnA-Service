package com.example.springqnaapp;

import com.example.springqnaapp.common.util.JwtTokenizer;
import com.example.springqnaapp.domain.RefreshToken;
import com.example.springqnaapp.domain.User;
import com.example.springqnaapp.repository.RefreshTokenRepository;
import com.example.springqnaapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
@EnableJpaAuditing
public class SpringQnaAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringQnaAppApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			PasswordEncoder passwordEncoder,
			UserRepository userRepository,
			JwtTokenizer jwtTokenizer,
			RefreshTokenRepository refreshTokenRepository
	) {
		return (args) -> {
			User oldUser = userRepository.findByUsername("login")
					.orElse(null);
			if (oldUser == null) {
				User user = userRepository.save(new User("login", "email@email.com", passwordEncoder.encode("1234")));
				String accessToken = jwtTokenizer.createAccessToken(user.getUsername(), user.getEmail(), Set.of("ROLE_USER"));
				String refreshToken = jwtTokenizer.createRefreshToken(user.getUsername(), user.getEmail(), Set.of("ROLE_USER"));
				refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken));

				System.out.println("\n\n\n");
				System.out.println(accessToken);
				System.out.println(refreshToken);
				System.out.println("\n\n\n");
			}
		};
	}
}
