package com.example.springqnaapp.controller;

import com.example.springqnaapp.common.dto.LoginResponseDto;
import com.example.springqnaapp.common.dto.UserRequestDto;
import com.example.springqnaapp.repository.RefreshTokenRepository;
import com.example.springqnaapp.service.RefreshTokenService;
import com.example.springqnaapp.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/login")
	public ResponseEntity<?> login(
			@RequestBody @Valid UserRequestDto userRequestDto,
			BindingResult bindingResult
	) {
		if (bindingResult.hasErrors())
			return ResponseEntity.badRequest()
			                     .body(bindingResult.getAllErrors());

		LoginResponseDto loginResponseDto = userService.login(userRequestDto.username(),
		                                                      userRequestDto.password());


		return ResponseEntity.ok(loginResponseDto);
	}

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @RequestBody Map<String,String> body
            ) {
        String refreshToken = body.get("refreshToken");
        Optional<String> newAccessToken
                = refreshTokenService.reissueRefreshToken(refreshToken);

        if(newAccessToken.isEmpty()){
            return ResponseEntity.badRequest().body("Invalid refresh token");
        }

        return ResponseEntity.ok(newAccessToken.get());


    }
}