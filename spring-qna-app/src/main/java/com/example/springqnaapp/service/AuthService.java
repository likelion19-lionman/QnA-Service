package com.example.springqnaapp.service;

import com.example.springqnaapp.common.dto.EmailCodeRequestDto;
import com.example.springqnaapp.common.dto.EmailVerifyRequestDto;
import com.example.springqnaapp.common.dto.TokensDto;
import com.example.springqnaapp.domain.User;
import com.example.springqnaapp.common.dto.RegisterRequestDto;
import jakarta.mail.MessagingException;

public interface AuthService {
    boolean checkDuplication(String username);

    User register(RegisterRequestDto requestDto);

    boolean sendAuthCode(EmailCodeRequestDto requestDto) throws MessagingException;

    boolean validateAuthCode(EmailVerifyRequestDto verifyDto);

    TokensDto login(String username, String password);

    void logout(String refreshToken);
}