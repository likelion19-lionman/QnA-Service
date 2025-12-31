package com.example.springqnaapp.repository;

import com.example.springqnaapp.domain.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {
    // 이메일로 인증 정보 조회
    Optional<EmailAuth> findByEmail(String email);
}
