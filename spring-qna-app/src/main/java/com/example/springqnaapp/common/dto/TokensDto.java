package com.example.springqnaapp.common.dto;

import jakarta.validation.constraints.NotEmpty;

public record TokensDto(
        @NotEmpty String accessToken,
        @NotEmpty String refreshToken
) { }
