package com.example.springqnaapp.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserRequestDto(
		@Min(4) String username,
		@Email String email,
		@NotNull @Pattern(regexp = "^(?=.*[^a-zA-Z0-9]).{8,}$") String password
) { }