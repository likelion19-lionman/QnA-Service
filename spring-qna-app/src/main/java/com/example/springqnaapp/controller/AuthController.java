package com.example.springqnaapp.controller;

import com.example.springqnaapp.common.dto.EmailCodeRequestDto;
import com.example.springqnaapp.common.dto.EmailVerifyRequestDto;
import com.example.springqnaapp.common.dto.LoginRequestDto;
import com.example.springqnaapp.common.dto.TokensDto;
import com.example.springqnaapp.common.util.CookieHandler;
import com.example.springqnaapp.common.util.JwtTokenizer;
import com.example.springqnaapp.common.dto.RegisterRequestDto;
import com.example.springqnaapp.repository.RefreshTokenRepository;
import com.example.springqnaapp.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	private final UserService userService;
	private final JwtTokenizer jwtTokenizer;
	private final CookieHandler cookieHandler;
	private final RefreshTokenRepository refreshTokenRepository;

	@PostMapping(
			value = "/check-duplication",
			consumes = "text/plain",
			produces = "application/json"
	)
	public ResponseEntity<?> checkDuplication(
			@RequestBody String username
	) {
        boolean checked = userService.checkDuplication(username);
        return checked
                ? ResponseEntity.ok().body("사용 가능한 아이디입니다.")
                : ResponseEntity.badRequest().body("이미 존재하는 아이디입니다.");
    }

	// 인증번호 전송
	@PostMapping(
			value = "/email/send",
			consumes = "application/json",
			produces = "application/json"
	)
	public ResponseEntity<?> sendAuthCode(
			@Valid
			@RequestBody
			EmailCodeRequestDto emailCodeRequestDto
	) {
		try {
			return userService.sendAuthCode(emailCodeRequestDto) ?
			       ResponseEntity.ok("인증코드가 전송되었습니다.") :
			       ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인증 코드 전송이 실패하였습니다.");
		} catch (IllegalArgumentException e) {
			// 이미 가입된 이메일
			return ResponseEntity.badRequest()
			                     .body(e.getMessage());
		} catch (MessagingException e) {
			// 메일 전송 오류
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			                     .body("이메일 전송 중 오류가 발생했습니다.");
		}
	}

	// 인증번호 검증
	@PostMapping(
			value = "/email/verify",
			consumes = "application/json",
			produces = "application/json"
	)
	public ResponseEntity<?> verifyAuthCode(
			@Valid
			@RequestBody
			EmailVerifyRequestDto emailVerifyRequestDto
	) {
		try {
			return userService.validateAuthCode(emailVerifyRequestDto) ?
			       ResponseEntity.ok("이메일 인증에 성공하였습니다.") :
			       ResponseEntity.badRequest().body("이메일 인증에 실패하였습니다.");
		} catch (IllegalArgumentException e) {
			// 인증 정보 없음
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 회원가입
	@PostMapping(
			value = "/register",
			consumes = "application/json",
			produces = "application/json"
	)
	public ResponseEntity<?> register(
			@Valid
			@RequestBody
			RegisterRequestDto registerRequestDto,
			HttpServletResponse response
	) {
		// 회원가입 처리
		try {
			userService.register(registerRequestDto);
			TokensDto tokens = userService.login(
					registerRequestDto.username(),
					registerRequestDto.password()
			);
			cookieHandler.createCookie(response, "accessToken", tokens.accessToken());
			return ResponseEntity.ok(tokens.refreshToken());
		} catch (IllegalArgumentException e) {
			// 이메일 미인증
			return ResponseEntity.badRequest()
			                     .body(e.getMessage());
		}
	}

	@PostMapping(
			value = "/login",
			consumes = "application/json",
			produces = "application/json"
	)
	public ResponseEntity<?> login(
			@RequestBody
			@Valid
			LoginRequestDto loginRequestDto,
			HttpServletResponse response
	) {
		TokensDto tokens = userService.login(
				loginRequestDto.username(),
				loginRequestDto.password()
		);
		cookieHandler.createCookie(response, "accessToken", tokens.accessToken());
		return ResponseEntity.ok(tokens.refreshToken());
	}

	@PostMapping(
			value = "/refresh",
			consumes = "text/plain",
			produces = "application/json"
	)
	public ResponseEntity<?> refresh(
			@RequestBody
			String invalidRefreshToken,
			HttpServletResponse response
	) {
		try {
			// 서버가 통제권을 가지고 있게 함. refresh 가 탈취 됐다면 refresh 를 DB 에서 삭제시켜
			// 해당 로직으로 인해 막게 끔 해야 함.
			var refreshToken = refreshTokenRepository.findByValue(invalidRefreshToken)
					.orElseThrow(() -> new IllegalArgumentException("토큰이 서버에 의해 차단되었습니다."));

			// 만료되었다면 catch 문으로
			Claims claim = jwtTokenizer.parseRefreshToken(refreshToken.getValue());

			// 제대로 파싱이 되었다면 AccessToken 을 만듦
			@SuppressWarnings("unchecked")
			String newAccessToken = jwtTokenizer.createAccessToken(
					claim.get("username", String.class),
					claim.get("email", String.class),
					(Set<String>) claim.get("roles", Set.class)
			);

			// 쿠키를 구워 반환
			cookieHandler.createCookie(response, "accessToken", newAccessToken);
			return ResponseEntity.noContent().build();
		} catch (ExpiredJwtException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			                     .body("Refresh Token 만료");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			                     .body(e.getMessage());
		}
	}
}
