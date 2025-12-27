package com.example.springqnaapp.service;

import com.example.springqnaapp.common.dto.EmailRequestDto;
import com.example.springqnaapp.common.dto.EmailVerifyDto;
import com.example.springqnaapp.common.dto.LoginResponseDto;
import com.example.springqnaapp.domain.User;
import com.example.springqnaapp.common.dto.UserRequestDto;
import jakarta.mail.MessagingException;

public interface UserService {
    boolean checkDuplication(String username);

    User register(UserRequestDto requestDto);

    boolean sendAuthCode(EmailRequestDto requestDto) throws MessagingException;

    boolean validateAuthCode(EmailVerifyDto verifyDto);
    
    LoginResponseDto login(String username, String password);
}
