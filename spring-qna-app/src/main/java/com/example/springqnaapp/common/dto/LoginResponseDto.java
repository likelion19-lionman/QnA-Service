package com.example.springqnaapp.common.dto;

public record LoginResponseDto(
		String accessToken,
		String refreshToken,
		String username
) { }