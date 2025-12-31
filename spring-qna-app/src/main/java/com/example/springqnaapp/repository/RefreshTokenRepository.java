package com.example.springqnaapp.repository;

import com.example.springqnaapp.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(Long userId);

    Optional<RefreshToken> findByValue(String value);

    void deleteByValue(String value);
}
