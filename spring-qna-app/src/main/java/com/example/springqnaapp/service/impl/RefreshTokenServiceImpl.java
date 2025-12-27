package com.example.springqnaapp.service.impl;

import com.example.springqnaapp.common.util.JwtTokenizer;
import com.example.springqnaapp.domain.RefreshToken;
import com.example.springqnaapp.domain.User;
import com.example.springqnaapp.repository.RefreshTokenRepository;
import com.example.springqnaapp.repository.UserRepository;
import com.example.springqnaapp.service.RefreshTokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtTokenizer jwtTokenizer;


    @Override // AccessToken 재발급
    public Optional<String> reissueRefreshToken(String refreshToken) {
        if(!jwtTokenizer.validateRefreshToken(refreshToken)) return Optional.empty();

        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);

        String username = claims.get("username", String.class);
        String email = claims.get("email", String.class);
        List<String> roles = claims.get("roles", List.class);


        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new NoSuchElementException("Username not found"));
        Long userId = user.getId();


        RefreshToken saved = refreshTokenRepository.findByUserId(userId).orElseThrow(() ->
               new NoSuchElementException("RefreshToken not found"));
        if(!saved.getValue().equals(refreshToken)) return Optional.empty();

        String newAccessToken = jwtTokenizer.createAccessToken(username, email,roles);

        return Optional.of(newAccessToken);
    }


    @Override  //db에 refreshToken 저장
    public void saveRefreshToken(String refreshToken) {
        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);
        String username = claims.get("username", String.class);

        User user = userRepository.findByUsername(username).orElseThrow(()->
                new  NoSuchElementException("Username not found"));


        refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken));

    }



}
