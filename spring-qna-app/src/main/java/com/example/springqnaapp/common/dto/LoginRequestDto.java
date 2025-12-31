package com.example.springqnaapp.common.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
		@Size(min = 4, message = "유저이름은 최소 4자 이상입니다.")
		String username,

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