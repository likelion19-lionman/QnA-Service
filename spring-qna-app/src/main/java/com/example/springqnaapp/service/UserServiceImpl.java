package com.example.springqnaapp.service;

import com.example.springqnaapp.domain.User;
import com.example.springqnaapp.dto.UserRequestDto;
import com.example.springqnaapp.repository.UserRepository;
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
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .email(requestDto.getEmail())
                .build();

        return userRepository.save(user);
    }
}
