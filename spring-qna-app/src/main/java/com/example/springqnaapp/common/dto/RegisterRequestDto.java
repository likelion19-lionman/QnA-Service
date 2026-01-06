package com.example.springqnaapp.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "회원가입 요청 데이터")
public record RegisterRequestDto(

		@Schema(
				description = "사용자 아이디 (4자 이상)",
				example = "newuser123",
				requiredMode = Schema.RequiredMode.REQUIRED,
				minLength = 4
		)
		@Size(min = 4, message = "유저이름은 최소 4자 이상입니다.")
		String username,

		@Schema(
				description = "이메일 인증 완료 여부",
				example = "true",
				requiredMode = Schema.RequiredMode.REQUIRED
		)
		Boolean isEmailVerified,

		@Schema(
				description = "사용자 이메일 주소",
				example = "newuser@example.com",
				requiredMode = Schema.RequiredMode.REQUIRED
		)
		@Email(message = "이메일 형태가 올바르지 않습니다.")
		@Pattern(
				regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
				message = "올바른 이메일 형식이 아닙니다."
		)
		String email,

		@Schema(
				description = "비밀번호 (8자 이상, 특수문자 1개 이상 포함)",
				example = "SecurePass123!",
				requiredMode = Schema.RequiredMode.REQUIRED,
				minLength = 8
		)
		@Size(
				min = 8,
				message = "비밀번호는 최소 8자 이상입니다."
		)
		@Pattern(
				regexp = "^(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/]).{8,}$",
				message = "비밀번호는 특수문자를 최소 1개 포함해야 합니다."
		)
		String password
) { }