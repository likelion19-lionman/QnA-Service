package com.example.springqnaapp.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "이메일 인증 확인 요청 데이터")
public record EmailVerifyRequestDto(

		@Schema(
				description = "인증을 진행할 이메일 주소",
				example = "user@example.com",
				requiredMode = Schema.RequiredMode.REQUIRED
		)
		@NotBlank(message = "이메일을 입력해주세요")
		@Email(message = "유효한 이메일 형식이 아닙니다")
		@Pattern(
				regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
				message = "올바른 이메일 형식이 아닙니다."
		)
		String email,

		@Schema(
				description = "이메일로 발송된 영문 대문자+숫자 조합 6자리 인증번호",
				example = "AB12C3",
				requiredMode = Schema.RequiredMode.REQUIRED,
				minLength = 6,
				maxLength = 6
		)
		@NotBlank(message = "인증번호를 입력해주세요")
		@Pattern(
				regexp = "^[A-Z0-9]{6}$",
				message = "인증번호는 대문자 영문자와 숫자로 이루어진 6자리여야 합니다."
		)
		String authCode
) { }