package com.example.springqnaapp.service;

import com.example.springqnaapp.domain.User;
import com.example.springqnaapp.dto.UserRequestDto;

public interface UserService {

    boolean checkDuplication(String username);

    User register(UserRequestDto requestDto);
}
