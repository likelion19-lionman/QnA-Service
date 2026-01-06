package com.example.springqnaapp.controller;

import com.example.springqnaapp.common.dto.EmailCodeRequestDto;
import com.example.springqnaapp.common.dto.EmailVerifyRequestDto;
import com.example.springqnaapp.common.dto.LoginRequestDto;
import com.example.springqnaapp.common.dto.TokensDto;
import com.example.springqnaapp.common.util.CookieHandler;
import com.example.springqnaapp.common.dto.RegisterRequestDto;
import com.example.springqnaapp.controller.docs.AuthController;
import com.example.springqnaapp.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.springqnaapp.common.dto.LogoutRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthControllerImpl implements AuthController {
	private final AuthService authService;
	private final CookieHandler cookieHandler;

	@Override
	@PostMapping(
			value = "/check-duplication",
			consumes = "text/plain",
			produces = "application/json"
	)
	public ResponseEntity<Boolean> checkDuplication(
			@RequestBody String username
	) {
		if (!username.matches("^[a-zA-Z0-9]{4,}$"))
			throw new IllegalArgumentException("사용자 이름은 4 글자 이상이어야 하며 영대소문자 및 숫자만 쓸 수 있습니다.");
		return ResponseEntity.ok(authService.checkDuplication(username));
    }

	// 인증번호 전송
	@Override
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
	@Override
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
	@Override
	@PostMapping(
			value = "/register",
			consumes = "application/json",
			produces = "text/plain"
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

	@Override
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

	@Override
    @PostMapping(
            value = "/logout",
            consumes = "application/json"
    )
    public ResponseEntity<Void> logout(
            @Valid
            @RequestBody
            LogoutRequestDto request,
            HttpServletResponse response
    ) {
        authService.logout(request.refreshToken());
        cookieHandler.deleteCookie(response, "accessToken");
        return ResponseEntity.noContent().build();
    }

	@Override
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
		if (refreshToken.isEmpty())
			throw new IllegalArgumentException("입력 값이 비어있습니다.");
		String accessToken = authService.refresh(refreshToken);
		cookieHandler.createCookie(response, "accessToken", accessToken);
		return ResponseEntity.noContent().build();
	}

    // 인증번호 재전송
	@Override
    @PostMapping(
            value = "/email/resend",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<Void> resendAuthCode(
            @Valid
            @RequestBody
            EmailCodeRequestDto emailCodeRequestDto
    ) {
        authService.resendAuthCode(emailCodeRequestDto);
        return ResponseEntity.noContent().build();
    }
}
