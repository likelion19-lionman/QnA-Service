package com.example.springqnaapp.service.impl;

import com.example.springqnaapp.common.dto.EmailCodeRequestDto;
import com.example.springqnaapp.common.dto.EmailVerifyRequestDto;
import com.example.springqnaapp.common.dto.RegisterRequestDto;
import com.example.springqnaapp.common.dto.TokensDto;
import com.example.springqnaapp.common.util.JwtTokenizer;
import com.example.springqnaapp.common.util.MailSender;
import com.example.springqnaapp.domain.EmailAuth;
import com.example.springqnaapp.domain.RefreshToken;
import com.example.springqnaapp.domain.Role;
import com.example.springqnaapp.domain.User;
import com.example.springqnaapp.repository.RefreshTokenRepository;
import com.example.springqnaapp.repository.EmailAuthRepository;
import com.example.springqnaapp.repository.UserRepository;
import com.example.springqnaapp.service.AuthService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenizer jwtTokenizer;
    private final Role defaultRole;
	private final EmailAuthRepository emailAuthRepository;
	private final MailSender mailSender;

	@Override
	@Transactional(readOnly = true)
	public boolean checkDuplication(String username) {
		return userRepository.existsByUsername(username);
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
	public User register(RegisterRequestDto requestDto) {

        // 1. username 중복 확인
        if (userRepository.existsByUsername(requestDto.username()))
            throw new IllegalStateException("이미 사용 중인 아이디입니다.");

        // 2. 이메일 인증 완료 확인
        if (!isEmailVerified(requestDto.email()))
            throw new IllegalStateException("이메일 인증이 완료되지 않았습니다.");

        // 3. 이메일 중복 확인
        if (userRepository.existsByEmail(requestDto.email()))
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");

        // 4. 사용자 정보 저장
        User savedUser = userRepository.save(new User(
                requestDto.username(),
                requestDto.email(),
                passwordEncoder.encode(requestDto.password()),
                defaultRole
        ));

        // 5. 인증 정보 삭제
        EmailAuth auth = emailAuthRepository.findByEmail(requestDto.email())
                .orElseThrow(() -> new IllegalArgumentException("시스템 오류"));

        if (auth != null)
            emailAuthRepository.delete(auth);

        return savedUser;
	}

    // 이메일 인증 완료 여부 확인 (인증 완료 + 유효기간 내)
    private boolean isEmailVerified(String email) {
        EmailAuth auth = emailAuthRepository.findByEmail(email).orElse(null);
        if (auth == null)
            return false;
        return auth.isVerified() && !auth.isExpired();
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
    public boolean sendAuthCode(EmailCodeRequestDto requestDto) throws MessagingException {

        String email = requestDto.email();

        // 1. 이미 가입된 이메일인지 확인
        if (userRepository.existsByEmail(email))
            throw new IllegalArgumentException("이미 가입 된 이메일입니다.");

        // 2. 인증번호 생성 및 메일 전송
        String authCode = mailSender.sendMessage(email);

        if (authCode != null) {
            emailAuthRepository.findByEmail(email).ifPresentOrElse(
                    emailAuth -> emailAuth.patch(authCode),
                    () -> emailAuthRepository.save(new EmailAuth(email, authCode))
            );
            return true;
        }
        return false;
    }

    /**
     * 인증번호 확인<br/>
     *<br/>
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
        EmailAuth auth = emailAuthRepository.findByEmail(verifyDto.email())
                .orElseThrow(() -> new IllegalArgumentException("인증 요청 내역이 없습니다."));

        // 2. 만료 여부 확인
        if (auth.isExpired()) {
            emailAuthRepository.delete(auth);
            throw new IllegalStateException("인증 시간이 만료되었습니다. 다시 시도해주세요.");
        }

        // 3. 인증번호 일치 여부 확인
        if (!auth.getAuthCode().equals(verifyDto.authCode()))
            return false;

        // 4. 인증 완료 처리
        return auth.verify();
	}

	@Override
	@Transactional
	public TokensDto login(String username, String password) {
		User user = userRepository.findByUsername(username).orElseThrow(() ->
				new IllegalArgumentException("존재하지 않는 회원입니다."));

		if (!passwordEncoder.matches(password, user.getPassword()))
			throw new IllegalArgumentException("존재하지 않는 회원입니다.");

		var roles = user.getStringRoles();

		String accessToken = jwtTokenizer.createAccessToken(user.getUsername(), user.getEmail(), roles);
		String refreshToken = jwtTokenizer.createRefreshToken(user.getUsername(), user.getEmail(), roles);

		refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken));

		return new TokensDto(accessToken, refreshToken);
	}
}