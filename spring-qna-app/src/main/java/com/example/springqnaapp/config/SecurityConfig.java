package com.example.springqnaapp.config;

import com.example.springqnaapp.security.jwt.JwtAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@Slf4j
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
												   JwtAuthenticationFilter jwtAuthenticationFilter) {
		return http.authorizeHttpRequests(auth -> auth
				           .requestMatchers("/test/**").authenticated()
				           .anyRequest().authenticated()
		           )
		           .httpBasic(AbstractHttpConfigurer::disable)
		           .formLogin(AbstractHttpConfigurer::disable)
		           .addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
		           .csrf(AbstractHttpConfigurer::disable)
		           .sessionManagement(session -> session
				           .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		           )
		           .cors(cors -> cors
				           .configurationSource(corsConfigurationSource())
		           )
		           .build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true);
		configuration.setAllowedOriginPatterns(List.of("http://localhost:3000"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));

		configuration.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}