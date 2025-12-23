package com.example.springqnaapp.service.impl;

import com.example.springqnaapp.common.dto.LoginResponseDto;
import com.example.springqnaapp.common.util.JwtTokenizer;
import com.example.springqnaapp.domain.RefreshToken;
import com.example.springqnaapp.domain.User;
import com.example.springqnaapp.repository.RefreshTokenRepository;
import com.example.springqnaapp.repository.UserRepository;
import com.example.springqnaapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenizer jwtTokenizer;

	@Override
	public boolean checkedDuplication(String username) {
		return false;
	}

	@Override
	public User register(String username, String password) {
		return null;
	}

	@Override
	public LoginResponseDto login(String username, String password) {
		User user = userRepository.findByUsername(username).orElseThrow(() ->
				new IllegalArgumentException("존재하지 않는 회원입니다."));

		if (!passwordEncoder.matches(password, user.getPassword()))
			throw new IllegalArgumentException("존재하지 않는 회원입니다.");

		String accessToken = jwtTokenizer.createAccessToken(
				user.getUsername(),
				user.getEmail(),
				List.of("ROLE_USER"));

		String refreshToken = jwtTokenizer.createRefreshToken(
				user.getUsername(),
				user.getEmail(),
				List.of("ROLE_USER"));

		refreshTokenRepository.save(
				new RefreshToken(user.getId(), refreshToken));

		return new LoginResponseDto(
				accessToken, refreshToken, user.getUsername()
		);
	}
}