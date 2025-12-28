package com.example.springqnaapp.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record RegisterRequestDto(
		@Min(value = 4, message = "유저이름은 최소 4자 이상입니다.")
		String username,

		@Email(message = "이메일 형태가 올바르지 않습니다.")
		@Pattern(
				regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$",
				message = "gmail 계정만 사용할 수 있습니다."
		)
		String email,

		@Min(
				value = 8,
				message = "비밀번호는 최소 8자 이상입니다."
		)
		@Pattern(
				regexp = "^(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/]).{8,}$",
				message = "비밀번호는 특수문자를 최소 1개 포함해야 합니다."
		)
		String password
) { }