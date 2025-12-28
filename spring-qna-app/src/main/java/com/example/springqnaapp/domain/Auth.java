package com.example.springqnaapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "email_auth")
@NoArgsConstructor
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 6)
    private String authCode;

    // 인증 완료 여부
    @Column(nullable = false)
    private boolean verified;

    // 만료 시간
    @Column(nullable = false)
    private LocalDateTime expireAt;

    public Auth(String email, String authCode) {
        this.email = email;
        this.authCode = authCode;
        this.verified = false;
        this.expireAt = LocalDateTime.now().plusMinutes(5);
    }

    // 인증번호 재발급
    public void patch(String authCode) {
        this.authCode = authCode;
        this.verified = false;
        this.expireAt = LocalDateTime.now().plusMinutes(5);
    }

    // 인증 완료
    public void verify() {
        this.verified = true;
    }

    // 유효 기간 만료
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireAt);
    }
}
