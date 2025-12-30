package com.example.springqnaapp.common.dto;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequestDto(
        @NotBlank String refreshToken
) {
}
