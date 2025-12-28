package com.example.springqnaapp.repository;

import com.example.springqnaapp.domain.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    // 이메일로 인증 정보 조회
    Auth findByEmail(String email);
}
