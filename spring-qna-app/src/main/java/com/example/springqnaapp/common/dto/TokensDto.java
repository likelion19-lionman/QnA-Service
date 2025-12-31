package com.example.springqnaapp.common.dto;

public record TokensDto(
        String accessToken,
		String refreshToken
) { }
