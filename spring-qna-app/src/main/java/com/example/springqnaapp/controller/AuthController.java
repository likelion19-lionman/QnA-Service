package com.example.springqnaapp.controller;

import com.example.springqnaapp.common.dto.EmailRequestDto;
import com.example.springqnaapp.common.dto.EmailVerifyDto;
import com.example.springqnaapp.domain.User;
import com.example.springqnaapp.common.dto.LoginResponseDto;
import com.example.springqnaapp.common.dto.UserRequestDto;
import com.example.springqnaapp.service.UserService;
import jakarta.validation.Valid;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @GetMapping("/check-duplication")
    public ResponseEntity<Boolean> checkDuplication(String username) {

        boolean checked = userService.checkDuplication(username);

        if (!checked) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }

        return ResponseEntity.ok(checked);
    }

    // 인증번호 전송
    @PostMapping("/email/send")
    public ResponseEntity<?> sendAuthCode(
            @Valid @RequestBody EmailRequestDto requestDto,
            BindingResult bindingResult
    ) {

        // email 필드만 유효성 검증
        if (bindingResult.hasFieldErrors("email")) {
            return ResponseEntity.badRequest()
                    .body(bindingResult.getFieldErrors("email").stream()
                            .map(err -> err.getDefaultMessage())
                            .collect(Collectors.toList())
                    );
        }

        try {
            boolean isSend = userService.sendAuthCode(requestDto);

            return isSend ?
                    ResponseEntity.ok("인증코드가 전송되었습니다.") :
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인증 코드 전송이 실패하였습니다.");
        } catch (IllegalArgumentException e) {
            // 이미 가입된 이메일
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (MessagingException e) {
            // 메일 전송 오류
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송 중 오류가 발생했습니다.");
        }
    }

    // 인증번호 검증
    @PostMapping("/email/verify")
    public ResponseEntity<?> verifyAuthCode(
            @Valid @RequestBody EmailVerifyDto verifyDto,
            BindingResult bindingResult
    ) {

        // 유효성 검증
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try {
            boolean isSuccess = userService.validateAuthCode(verifyDto);

            return isSuccess ?
                    ResponseEntity.ok("이메일 인증에 성공하였습니다.") :
                    ResponseEntity.badRequest().body("이메일 인증에 실패하였습니다.");
        } catch (IllegalArgumentException e) {
            // 인증 정보 없음
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody UserRequestDto requestDto,
            HttpServletResponse response,
            BindingResult bindingResult
    ) throws IOException {

        // 입력값 유효성 검증
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        // 회원가입 처리
        try {
            User user = userService.register(requestDto);
            response.sendRedirect("/auth/login"); // 리다이렉트?
            // FIXME: 로그인 메서드와 결합하여 로그인 화면으로 넘어갈 수 있는지 확인
            return ResponseEntity.status(201).body(user);
        } catch (IllegalArgumentException e) {
            // 이메일 미인증
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody @Valid UserRequestDto userRequestDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest()
                    .body(bindingResult.getAllErrors());

        LoginResponseDto loginResponseDto = userService.login(userRequestDto.username(),
                userRequestDto.password());

        return ResponseEntity.ok(loginResponseDto);
    }
}
