package com.example.springqnaapp.controller;

import com.example.springqnaapp.domain.User;
import com.example.springqnaapp.dto.UserRequestDto;
import com.example.springqnaapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @GetMapping("/check-duplication")
    public ResponseEntity<Boolean> checkDuplication(String username) {
        boolean checked = userService.checkDuplication(username);
        return checked ?
                ResponseEntity.ok(checked) :
                ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserRequestDto requestDto) {
        User user = userService.register(requestDto);
        return ResponseEntity.status(201).body(user);
    }
}
