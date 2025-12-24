package com.example.springqnaapp.controller;

import com.example.springqnaapp.common.dto.LoginResponseDto;
import com.example.springqnaapp.common.dto.UserRequestDto;
import com.example.springqnaapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	private final UserService userService;

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