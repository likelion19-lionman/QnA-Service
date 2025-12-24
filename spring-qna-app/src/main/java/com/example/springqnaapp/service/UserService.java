package com.example.springqnaapp.service;

import com.example.springqnaapp.common.dto.LoginResponseDto;
import com.example.springqnaapp.domain.User;

public interface UserService {
	boolean checkedDuplication(String username);
	User register(String username, String password);
	LoginResponseDto login(String username, String password);
}
