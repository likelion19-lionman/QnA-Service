package com.example.springqnaapp.security;

import com.example.springqnaapp.security.jwt.JwtErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request,
	                     HttpServletResponse response,
	                     AuthenticationException authException
	) throws IOException, ServletException {

		// 1. getParameter가 아니라 getAttribute로 가져와야 합니다.
		Object error = request.getAttribute("error");
		JwtErrorCode errorCode;

		// 2. null 체크 및 캐스팅 로직
		if (error instanceof JwtErrorCode) {
			errorCode = (JwtErrorCode) error;
		} else {
			// 토큰이 아예 없거나 알 수 없는 에러인 경우의 기본값
			errorCode = JwtErrorCode.JWT_UNKNOWN;
		}

		log.error("인증 실패 응답 송신 :: 코드: {}, 메시지: {}", errorCode.getCode(), authException.getMessage());

		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		HashMap<String, Object> errorInfo = new HashMap<>();
		errorInfo.put("message", errorCode.getMessage());
		errorInfo.put("code", errorCode.getCode());

		String responseJson = objectMapper.writeValueAsString(errorInfo);
		response.getWriter().write(responseJson);
	}
}