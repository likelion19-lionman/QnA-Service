package com.example.springqnaapp.service.impl;

import com.example.springqnaapp.common.dto.EmailCodeRequestDto;
import com.example.springqnaapp.common.dto.EmailVerifyRequestDto;
import com.example.springqnaapp.common.dto.RegisterRequestDto;
import com.example.springqnaapp.common.dto.TokensDto;
import com.example.springqnaapp.common.exception.ApiServerUnhealthyException;
import com.example.springqnaapp.common.exception.EmailCodeExpiredException;
import com.example.springqnaapp.common.exception.NotVerifiedException;
import com.example.springqnaapp.common.exception.UserExistedException;
import com.example.springqnaapp.common.exception.UserNotFoundException;
import com.example.springqnaapp.common.util.JwtTokenizer;
import com.example.springqnaapp.common.util.MailSender;
import com.example.springqnaapp.domain.EmailAuth;
import com.example.springqnaapp.domain.RefreshToken;
import com.example.springqnaapp.domain.Role;
import com.example.springqnaapp.domain.RoleEnum;
import com.example.springqnaapp.domain.User;
import com.example.springqnaapp.repository.EmailAuthRepository;
import com.example.springqnaapp.repository.RefreshTokenRepository;
import com.example.springqnaapp.repository.RoleRepository;
import com.example.springqnaapp.repository.UserRepository;
import com.example.springqnaapp.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private static Role DEFAULT_ROLE = null;
	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenizer jwtTokenizer;
	private final EmailAuthRepository emailAuthRepository;
	private final MailSender mailSender;
	private final RoleRepository roleRepository;

	@Override
	@Transactional(readOnly = true)
	public boolean checkDuplication(String username) {
		return !userRepository.existsByUsername(username);
	}

	/**
	 * 인증번호 이메일 전송<br/>
	 * <br/>
	 * 프로세스:<br/>
	 * 1. 이미 가입된 이메일인지 확인<br/>
	 * 2. 인증번호 생성 및 메일 전송<br/>
	 * 3. Auth 엔티티에 저장<br/>
	 */
	@Override
	@Transactional
	public void sendAuthCode(EmailCodeRequestDto requestDto) {
		String email = requestDto.email();

		// 1. 이미 가입된 이메일인지 확인
		if (userRepository.existsByEmail(email))
			throw new UserExistedException("이미 가입된 이메일입니다.");

		EmailAuth emailAuth = emailAuthRepository.findByEmail(email).orElse(null);
		if (isVerifying(emailAuth))
			throw new UserExistedException("현재 인증 진행중인 이메일입니다.");

		// 2. 인증번호 생성 및 메일 전송
		final var authCode = mailSender.sendMessage(email);

		if (authCode == null)
			throw new ApiServerUnhealthyException("인증 코드 전송이 실패하였습니다.");

		if (emailAuth == null)
			emailAuthRepository.save(new EmailAuth(email, authCode));
		else
			emailAuth.patch(authCode);
	}
	private boolean isVerifying(EmailAuth emailAuth) {
		return emailAuth != null // 이메일이 등록되어 있고,
		       && LocalDateTime.now().isBefore(emailAuth.getExpireAt()); // 이메일이 아직 만료되지 않았다면
	}

	/**
	 * 인증번호 확인<br/>
	 * <br/>
	 * 검증 순서:<br/>
	 * 1. 인증 정보 존재 확인<br/>
	 * 2. 만료 여부 확인<br/>
	 * 3. 인증번호 일치 여부 확인<br/>
	 * 4. 인증 완료 처리 (verified = true)<br/>
	 */
	@Override
	@Transactional
	public boolean validateAuthCode(EmailVerifyRequestDto verifyDto) {
		// 1. 인증 정보 존재 확인
		EmailAuth auth = emailAuthRepository
				.findByEmail(verifyDto.email())
				.orElseThrow(() -> new UserNotFoundException("인증 요청 내역이 없습니다."));

		// 2. 만료 여부 확인
		if (auth.isExpired()) {
			emailAuthRepository.delete(auth);
			throw new EmailCodeExpiredException("인증 시간이 만료되었습니다. 다시 시도해주세요.");
		}

		// 3. 인증번호 일치 여부 확인
		if (!auth.getAuthCode().equals(verifyDto.authCode()))
			return false;

		// 4. 인증 완료 처리
		auth.verify();
		emailAuthRepository.delete(auth);
		return true;
	}

	/**
	 * 회원가입 <br/>
	 * <br/>
	 * 프로세스:<br/>
	 * 1. username 중복 확인<br/>
	 * 2. 이메일 인증 완료 확인<br/>
	 * 3. 이메일 중복 확인<br/>
	 * 4. 사용자 정보 저장<br/>
	 * 5. 인증 정보 삭제<br/>
	 */
	@Override
	@Transactional
	public void register(RegisterRequestDto requestDto) {
		// 1. username 중복 확인
		if (userRepository.existsByUsername(requestDto.username()))
			throw new UserExistedException("이미 사용 중인 아이디입니다.");

		// 2. 이메일 인증 확인
		if (!requestDto.isEmailVerified())
			throw new NotVerifiedException("이메일 인증을 해주세요.");

		// 3. 이메일 중복 확인
		if (userRepository.existsByEmail(requestDto.email()))
			throw new UserExistedException("이미 사용 중인 이메일입니다.");

		if (DEFAULT_ROLE == null)
			DEFAULT_ROLE = roleRepository.findByRole(RoleEnum.ROLE_USER).get();

		// 4. 사용자 정보 저장
		userRepository.save(new User(
				requestDto.username(),
				requestDto.email(),
				passwordEncoder.encode(requestDto.password()),
				DEFAULT_ROLE
		));
	}

	@Override
	@Transactional
	public TokensDto login(String username, String password) {
		User user = userRepository.findByUsername(username).orElse(null);

		if (user == null || !passwordEncoder.matches(password, user.getPassword()))
			throw new UserNotFoundException("존재하지 않는 회원입니다.");

		Set<String> roles = user.getStringRoles();
		String accessToken = jwtTokenizer.createAccessToken(user.getUsername(), user.getEmail(), roles);
		String refreshToken = refreshTokenRepository.findByUserId(user.getId())
		                                         .map(RefreshToken::getValue)
		                                         .orElse(null);

		if (refreshToken == null) {
			refreshToken = jwtTokenizer.createRefreshToken(user.getUsername(), user.getEmail(), roles);
			refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken));
		}

		return new TokensDto(
				accessToken,
				refreshToken
		);
	}

	@Override
	@Transactional
	public void logout(String refreshTokenStr) {
		refreshTokenRepository.deleteByValue(refreshTokenStr);
	}

	@Override
	@Transactional
	public String refresh(String refreshTokenStr) {
		var refreshToken = refreshTokenRepository
				.findByValue(refreshTokenStr)
				.orElseThrow(() -> new JwtException("토큰이 유효하지 않습니다."));

		Claims claim = jwtTokenizer.parseRefreshToken(refreshToken.getValue());

		@SuppressWarnings("unchecked")
		List<String> rolesList = claim.get("roles", List.class);

		return jwtTokenizer.createAccessToken(
				claim.get("username", String.class),
				claim.get("email", String.class),
				new HashSet<>(rolesList)
		);
	}
}