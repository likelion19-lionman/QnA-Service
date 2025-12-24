package com.example.springqnaapp.controller;

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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDto requestDto,
                                      HttpServletResponse response,
                                      BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        User user = userService.register(requestDto);
        response.sendRedirect("/auth/login");
        // FIXME: 로그인 메서드와 결합하여 로그인 화면으로 넘어갈 수 있는지 확인
        return ResponseEntity.status(201).body(user);
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<String> requestAuthCode(@PathVariable String email) throws MessagingException {

        boolean isSend = userService.sendAuthCode(email);

        return isSend ?
                ResponseEntity.ok("인증코드가 전송되었습니다.") :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인증 코드 전송이 실패하였습니다.");
    }

    @PostMapping("/email")
    public ResponseEntity<String> validateAuthCode(@RequestParam(name = "email") String email,
                                                   @RequestParam(name = "auth") String authCode) {

        boolean isSuccess = userService.validateAuthCode(email, authCode);

        return isSuccess ?
                ResponseEntity.ok("이메일 인증에 성공하였습니다.") :
                ResponseEntity.badRequest().body("이메일 인증에 실패하였습니다.");
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
