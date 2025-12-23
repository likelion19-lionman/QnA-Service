package com.example.springqnaapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record UserRequestDto(
        @Min(value = 4,
                message = "유저이름은 최소 4자 이상입니다.")
        String username,

        @Min(value = 8,
                message = "비밀번호는 최소 8자 이상입니다.")
        @Pattern(regexp = "^(?=.*[^a-zA-Z0-9]).{8,}$",
                message = "비밀번호 형식이 올바르지 않습니다.")
        String password,

        @Email(message = "이메일 형태가 올바르지 않습니다.")
        @Pattern(
                regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$",
                message = "gmail 계정만 사용할 수 있습니다.")
        String email
) {

}
