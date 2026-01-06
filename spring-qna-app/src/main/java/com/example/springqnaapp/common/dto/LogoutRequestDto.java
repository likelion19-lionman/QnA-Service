package com.example.springqnaapp.common.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그아웃 요청 데이터 (토큰 무효화)")
public record LogoutRequestDto(

		@Schema(
				description = "로그아웃할 사용자의 Refresh Token",
				example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
				requiredMode = Schema.RequiredMode.REQUIRED
		)
		@NotBlank(message = "리프레시 토큰이 누락되었습니다.")
		String refreshToken
) { }