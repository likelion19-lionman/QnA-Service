package com.example.springqnaapp.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface RefreshTokenService{ ;
 Optional<String> reissueRefreshToken(String refreshToken);
 void saveRefreshToken(String refreshToken);
}
