package com.example.springqnaapp.service.impl;

import com.example.springqnaapp.common.dto.LoginResponseDto;
import com.example.springqnaapp.common.dto.UserRequestDto;
import com.example.springqnaapp.common.util.JwtTokenizer;
import com.example.springqnaapp.domain.Auth;
import com.example.springqnaapp.domain.RefreshToken;
import com.example.springqnaapp.domain.User;
import com.example.springqnaapp.repository.AuthRepository;
import com.example.springqnaapp.repository.RefreshTokenRepository;
import com.example.springqnaapp.repository.UserRepository;
import com.example.springqnaapp.service.MailService;
import com.example.springqnaapp.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenizer jwtTokenizer;
	private final AuthRepository authRepository;
	private final MailService mailService;

	@Override
	@Transactional(readOnly = true)
	public boolean checkDuplication(String username) {
		Optional<User> byUsername = userRepository.findByUsername(username);
		return byUsername.isEmpty();
	}

	@Override
	@Transactional
	public User register(UserRequestDto requestDto) {
		User user = User.builder()
		                .username(requestDto.username())
		                .password(passwordEncoder.encode(requestDto.password()))
		                .email(requestDto.email())
		                .build();

		return userRepository.save(user);
	}

	@Override
	@Transactional
	public boolean sendAuthCode(String email) throws MessagingException {
		String authCode = mailService.sendSimpleMessage(email);
		if (authCode != null) {
			Auth auth = authRepository.findByEmail(email);
			if (auth == null) {
				authRepository.save(new Auth(email, authCode));
			} else {
				auth.patch(authCode);
			}
			return true;
		}
		return false;
	}

	// 이메일과 인증코드 검증
	@Override
	public boolean validateAuthCode(String email, String authCode) {
		Auth auth = authRepository.findByEmail(email);
		if (auth != null && auth.getAuthCode().equals(authCode)) {
			authRepository.delete(auth);
			return true;
		}
		return false;
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