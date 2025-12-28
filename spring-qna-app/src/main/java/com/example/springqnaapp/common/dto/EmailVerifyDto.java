package com.example.springqnaapp.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmailVerifyDto(
        @NotBlank(message = "이메일을 입력해주세요")
        @Email(message = "유효한 이메일 형식이 아닙니다")
        @Pattern(
                regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$",
                message = "Gmail 계정만 사용할 수 있습니다"
        )
        String email,

        @NotBlank(message = "인증번호를 입력해주세요")
        @Pattern(
                regexp = "^[A-Z0-9]{6}$",
                message = "인증번호는 대문자 영문자와 숫자로 이루어진 6자리여야 합니다."
        )
        String authCode
) { }