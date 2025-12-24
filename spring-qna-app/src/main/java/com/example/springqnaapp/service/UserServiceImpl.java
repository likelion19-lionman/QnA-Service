package com.example.springqnaapp.service;

import com.example.springqnaapp.domain.Auth;
import com.example.springqnaapp.domain.User;
import com.example.springqnaapp.dto.UserRequestDto;
import com.example.springqnaapp.repository.AuthRepository;
import com.example.springqnaapp.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthRepository authRepository;
    private final MailService mailService;

    @Override
    @Transactional(readOnly = true)
    public boolean checkDuplication(String username) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        return byUsername.isEmpty();
    }

    @Override
    @Transactional
    public User register(UserRequestDto requestDto) {
        User user = User.builder()
                .username(requestDto.username())
                .password(passwordEncoder.encode(requestDto.password()))
                .email(requestDto.email())
                .build();

        return userRepository.save(user);
    }

    // 인증코드 발급
    @Override
    @Transactional
    public boolean sendAuthCode(String email) throws MessagingException {
        String authCode = mailService.sendSimpleMessage(email);
        if (authCode != null) {
            Auth auth = authRepository.findByEmail(email);
            if (auth == null) {
                authRepository.save(new Auth(email, authCode));
            } else {
                auth.patch(authCode);
            }
            return true;
        }
        return false;
    }

    // 이메일과 인증코드 검증
    @Override
    public boolean validateAuthCode(String email, String authCode) {
        Auth auth = authRepository.findByEmail(email);
        if (auth != null && auth.getAuthCode().equals(authCode)) {
            authRepository.delete(auth);
            return true;
        }
        return false;
    }
}
