package com.example.springqnaapp.service;

import com.example.springqnaapp.common.dto.LoginResponseDto;
import com.example.springqnaapp.domain.User;
import com.example.springqnaapp.common.dto.UserRequestDto;
import jakarta.mail.MessagingException;

public interface UserService {
    boolean checkDuplication(String username);

    User register(UserRequestDto requestDto);

    boolean sendAuthCode(String email) throws MessagingException;

    boolean validateAuthCode(String email, String authCode);
    
    LoginResponseDto login(String username, String password);
}
