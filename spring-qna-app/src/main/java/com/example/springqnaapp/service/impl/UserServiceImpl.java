package com.example.springqnaapp.service.impl;

import com.example.springqnaapp.common.dto.EmailCodeRequestDto;
import com.example.springqnaapp.common.dto.EmailVerifyRequestDto;
import com.example.springqnaapp.common.dto.RegisterRequestDto;
import com.example.springqnaapp.common.dto.TokensDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
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

    /**
     * 회원가입
     * <p>
     * 프로세스:
     * 1. username 중복 확인
     * 2. 이메일 인증 완료 확인
     * 3. 이메일 중복 확인
     * 4. 사용자 정보 저장
     * 5. 인증 정보 삭제
     */
    @Override
    @Transactional
    public User register(RegisterRequestDto requestDto) {

        // 1. username 중복 확인
        if (userRepository.findByUsername(requestDto.username()).isPresent()) {
            throw new IllegalStateException("이미 사용 중인 아이디입니다.");
        }

        // 2. 이메일 인증 완료 확인
        if (!isEmailVerified(requestDto.email())) {
            throw new IllegalStateException("이메일 인증이 완료되지 않았습니다.");
        }

        // 3. 이메일 중복 확인
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }

        // 4. 사용자 정보 저장
        User user = new User(requestDto.username(),
                requestDto.email(),
                passwordEncoder.encode(requestDto.password()));

        User savedUser = userRepository.save(user);

        // 5. 인증 정보 삭제
        Auth auth = authRepository.findByEmail(requestDto.email());

        if (auth != null) {
            authRepository.delete(auth);
        }

        return savedUser;
    }

    // 이메일 인증 완료 여부 확인 (인증 완료 + 유효기간 내)
    private boolean isEmailVerified(String email) {
        Auth auth = authRepository.findByEmail(email);
        if (auth == null) {
            return false;
        }
        return auth.isVerified() && !auth.isExpired();
    }

    /**
     * 인증번호 이메일 전송
     * <p>
     * 프로세스:
     * 1. 이미 가입된 이메일인지 확인
     * 2. 인증번호 생성 및 메일 전송
     * 3. Auth 엔티티에 저장
     */
    @Override
    @Transactional
    public boolean sendAuthCode(EmailCodeRequestDto requestDto) throws MessagingException {
        String email = requestDto.email();

        // 1. 이미 가입된 이메일인지 확인
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 2. 인증번호 생성 및 메일 전송
        String authCode = mailService.sendSimpleMessage(email);

        if (authCode != null) {
            Auth auth = authRepository.findByEmail(email);
            if (auth == null) {
                // 최초 인증 요청 -> 새로 생성
                authRepository.save(new Auth(email, authCode));
            } else {
                // 재전송 요청 -> 기존 정보 갱신
                auth.patch(authCode);
            }
            return true;
        }
        return false;
    }

    /**
     * 인증번호 확인
     * <p>
     * 검증 순서:
     * 1. 인증 정보 존재 확인
     * 2. 만료 여부 확인
     * 3. 인증번호 일치 여부 확인
     * 4. 인증 완료 처리 (verified = true)
     */
    @Override
    @Transactional
    public boolean validateAuthCode(EmailVerifyRequestDto verifyDto) {

        String email = verifyDto.email();
        String authCode = verifyDto.authCode();

        // 1. 인증 정보 존재 확인
        Auth auth = authRepository.findByEmail(email);
        if (auth == null) {
            throw new IllegalArgumentException("인증 요청 내역이 없습니다.");
        }

        // 2. 만료 여부 확인
        if (auth.isExpired()) {
            authRepository.delete(auth);
            throw new IllegalStateException("인증 시간이 만료되었습니다. 다시 시도해주세요.");
        }

        // 3. 인증번호 일치 여부 확인
        if (!auth.getAuthCode().equals(authCode)) {
            return false;
        }

        // 4. 인증 완료 처리
        auth.verify();
        return true;
    }

    @Override
    @Transactional
    public TokensDto login(String username, String password) {
        log.info("=== 로그인 서비스 실행중..., username: {}, password: {}", username, password);
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 회원입니다."));

        if (user == null)
            log.info("=== User 가 null 입니다!!!!!!");

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.info("=== 패스워드 매칭 오류!");
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

        log.info("=== 로그인 서비스 실행중..., username: {}, password: {}", username, password);
        var roles = user.getStringRoles();

        log.info("=== AccessToken 발급중...");
        String accessToken = jwtTokenizer.createAccessToken(user.getUsername(), user.getEmail(), roles);

        var optional = refreshTokenRepository.findByUserId(user.getId());

        if (optional.isPresent())
            return new TokensDto(
                    accessToken,
                    optional.get().getValue()
            );

        String refreshToken = jwtTokenizer.createRefreshToken(user.getUsername(), user.getEmail(), roles);
        refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken));
        return new TokensDto(accessToken, refreshToken);
    }
}