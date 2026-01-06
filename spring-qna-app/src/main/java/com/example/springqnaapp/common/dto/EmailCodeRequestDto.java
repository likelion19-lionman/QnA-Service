package com.example.springqnaapp.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "이메일 인증 코드 요청 데이터")
public record EmailCodeRequestDto(

		@Schema(
				description = "인증 코드를 받을 사용자의 이메일 주소",
				example = "user@example.com",
				requiredMode = Schema.RequiredMode.REQUIRED
		)
		@NotBlank(message = "이메일을 입력해주세요")
		@Email(message = "유효한 이메일 형식이 아닙니다")
		@Pattern(
				regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
				message = "올바른 이메일 형식이 아닙니다."
		)
		String email
) {
}