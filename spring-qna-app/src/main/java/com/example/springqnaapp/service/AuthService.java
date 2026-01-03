package com.example.springqnaapp.service;

import com.example.springqnaapp.common.dto.EmailCodeRequestDto;
import com.example.springqnaapp.common.dto.EmailVerifyRequestDto;
import com.example.springqnaapp.common.dto.TokensDto;
import com.example.springqnaapp.common.dto.RegisterRequestDto;

public interface AuthService {
    boolean checkDuplication(String username);

    void register(RegisterRequestDto requestDto);

    void sendAuthCode(EmailCodeRequestDto requestDto);

    boolean validateAuthCode(EmailVerifyRequestDto verifyDto);

    TokensDto login(String username, String password);

    void logout(String refreshTokenStr);

    String refresh(String refreshTokenStr);
}