package com.example.springqnaapp.controller;

import com.example.springqnaapp.common.dto.EmailRequestDto;
import com.example.springqnaapp.common.dto.EmailVerifyDto;
import com.example.springqnaapp.common.dto.LoginRequestDto;
import com.example.springqnaapp.domain.User;
import com.example.springqnaapp.common.dto.LoginResponseDto;
import com.example.springqnaapp.common.dto.RegisterRequestDto;
import com.example.springqnaapp.service.UserService;
import jakarta.validation.Valid;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	private final UserService userService;

	@PostMapping(
			value = "/check-duplication",
			consumes = "text/plain",
			produces = "application/json"
	)
	public ResponseEntity<Boolean> checkDuplication(
			@RequestBody String username
	) {
		return ResponseEntity.ok(userService.checkDuplication(username));
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
			EmailRequestDto emailRequestDto
	) {
		try {
			return userService.sendAuthCode(emailRequestDto) ?
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
			EmailVerifyDto emailVerifyDto
	) {
		try {
			return userService.validateAuthCode(emailVerifyDto) ?
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
			RegisterRequestDto registerRequestDto
	) {
		// 회원가입 처리
		try {
			User user = userService.register(registerRequestDto);

			// 자동 로그인 처리
			LoginResponseDto loginResponseDto = userService.login(
					registerRequestDto.username(),
					registerRequestDto.password()
			);
			return ResponseEntity.ok(loginResponseDto);
		} catch (IllegalArgumentException e) {
			// 이메일 미인증
			return ResponseEntity.badRequest()
			                     .body(e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(
			@RequestBody
			@Valid
			LoginRequestDto loginRequestDto
	) {
		LoginResponseDto loginResponseDto = userService.login(
				loginRequestDto.username(),
				loginRequestDto.password()
		);
		return ResponseEntity.ok(loginResponseDto);
	}
}
