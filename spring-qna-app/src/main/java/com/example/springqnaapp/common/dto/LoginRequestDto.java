package com.example.springqnaapp.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "로그인 요청 데이터")
public record LoginRequestDto(

		@Schema(
				description = "사용자 아이디 (최소 4자 이상)",
				example = "user1234",
				requiredMode = Schema.RequiredMode.REQUIRED,
				minLength = 4
		)
		@Size(min = 4, message = "유저이름은 최소 4자 이상입니다.")
		String username,

		@Schema(
				description = "비밀번호 (최소 8자, 특수문자 1개 이상 포함)",
				example = "password123!",
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