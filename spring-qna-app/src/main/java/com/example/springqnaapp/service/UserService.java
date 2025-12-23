package com.example.springqnaapp.service;

import com.example.springqnaapp.domain.User;
import com.example.springqnaapp.dto.UserRequestDto;
import jakarta.mail.MessagingException;

public interface UserService {

    boolean checkDuplication(String username);

    User register(UserRequestDto requestDto);

    boolean sendAuthCode(String email) throws MessagingException;

    boolean validationAuthCode(String email, String authCode);
}
