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
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

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
			RefreshTokenRepository refreshTokenRepository,
            RoleRepository roleRepository
	) {
		return (args) -> {
            roleRepository.saveAllAndFlush(
                    List.of(
                            new Role(RoleEnum.ROLE_USER),
                            new Role(RoleEnum.ROLE_ADMIN)
                    )
            );

            var userRole = roleRepository.findByRole(RoleEnum.ROLE_USER).get();
            var adminRole = roleRepository.findByRole(RoleEnum.ROLE_ADMIN).get();

            User user = userRepository.save(
                    new User("login",
                            "email@email.com",
                            passwordEncoder.encode("123456abc!"),
                            userRole
                    )
            );

            User admin = userRepository.save(
                    new User("admin",
                            "admin@email.com",
                            passwordEncoder.encode("admin123!"),
                            adminRole
                    )
            );


            String accessToken = jwtTokenizer.createAccessToken(user.getUsername(), user.getEmail(), user.getStringRoles());
            String refreshToken = jwtTokenizer.createRefreshToken(user.getUsername(), user.getEmail(), user.getStringRoles());

            refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken));

            System.out.println("========== User =========");
            System.out.println("accessToken : " + accessToken);
            System.out.println("refreshToken : " + refreshToken);
            System.out.println("=========================");

            String adminAccessToken = jwtTokenizer.createAccessToken(admin.getUsername(), admin.getEmail(), admin.getStringRoles());
            String adminRefreshToken = jwtTokenizer.createRefreshToken(admin.getUsername(), admin.getEmail(), admin.getStringRoles());

            refreshTokenRepository.save(new RefreshToken(admin.getId(), adminRefreshToken));

            System.out.println("========== Admin =========");
            System.out.println("accessToken : " +adminAccessToken);
            System.out.println("refreshToken : " + adminRefreshToken);
            System.out.println("=========================");
		};
	}
}
