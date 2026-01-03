package com.example.springqnaapp.controller;

import com.example.springqnaapp.common.dto.EmailCodeRequestDto;
import com.example.springqnaapp.common.dto.EmailVerifyRequestDto;
import com.example.springqnaapp.common.dto.LoginRequestDto;
import com.example.springqnaapp.common.dto.TokensDto;
import com.example.springqnaapp.common.util.CookieHandler;
import com.example.springqnaapp.common.util.JwtTokenizer;
import com.example.springqnaapp.common.dto.RegisterRequestDto;
import com.example.springqnaapp.repository.RefreshTokenRepository;
import com.example.springqnaapp.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.springqnaapp.common.dto.LogoutRequestDto;

import java.util.HashSet;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;
	private final JwtTokenizer jwtTokenizer;
	private final CookieHandler cookieHandler;
	private final RefreshTokenRepository refreshTokenRepository;

	@PostMapping(
			value = "/check-duplication",
			consumes = "text/plain",
			produces = "application/json"
	)
	public ResponseEntity<Boolean> checkDuplication(
			@RequestBody String username
	) {
		return ResponseEntity.ok(authService.checkDuplication(username));
    }

	// 인증번호 전송
	@PostMapping(
			value = "/email/send",
			consumes = "application/json",
			produces = "application/json"
	)
	public ResponseEntity<Void> sendAuthCode(
			@Valid
			@RequestBody
			EmailCodeRequestDto emailCodeRequestDto
	) {
		authService.sendAuthCode(emailCodeRequestDto);
		return ResponseEntity.noContent().build();
	}

	// 인증번호 검증
	@PostMapping(
			value = "/email/verify",
			consumes = "application/json",
			produces = "application/json"
	)
	public ResponseEntity<Boolean> verifyAuthCode(
			@Valid
			@RequestBody
			EmailVerifyRequestDto emailVerifyRequestDto
	) {
		return ResponseEntity.ok(authService.validateAuthCode(emailVerifyRequestDto));
	}

	// 회원가입
	@PostMapping(
			value = "/register",
			consumes = "application/json",
			produces = "application/json"
	)
	public ResponseEntity<String> register(
			@Valid
			@RequestBody
			RegisterRequestDto registerRequestDto,
			HttpServletResponse response
	) {
        authService.register(registerRequestDto);
		TokensDto tokens = authService.login(
				registerRequestDto.username(),
				registerRequestDto.password()
		);
		cookieHandler.createCookie(response, "accessToken", tokens.accessToken());
		return ResponseEntity.ok(tokens.refreshToken());
	}

	@PostMapping(
			value = "/login",
			consumes = "application/json",
			produces = "text/plain"
	)
	public ResponseEntity<String> login(
			@RequestBody
			@Valid
			LoginRequestDto loginRequestDto,
			HttpServletResponse response
	) {
		TokensDto tokens = authService.login(
				loginRequestDto.username(),
				loginRequestDto.password()
		);
		cookieHandler.createCookie(response, "accessToken", tokens.accessToken());
		return ResponseEntity.ok(tokens.refreshToken());
	}

    @PostMapping(
            value = "/logout",
            consumes = "application/json"
    )
    public ResponseEntity<Void> logout(
            @RequestBody LogoutRequestDto request,
            HttpServletResponse response
    ) {
        authService.logout(request.refreshToken());
        cookieHandler.deleteCookie(response, "accessToken");
        return ResponseEntity.noContent().build();
    }

	@PostMapping(
			value = "/refresh",
			consumes = "text/plain",
			produces = "application/json"
	)
	public ResponseEntity<Void> refresh(
			@RequestBody
			String refreshToken,
			HttpServletResponse response
	) {
		String accessToken = authService.refresh(refreshToken);
		cookieHandler.createCookie(response, "accessToken", accessToken);
		return ResponseEntity.noContent().build();
	}
}
