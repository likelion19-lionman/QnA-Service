package com.example.springqnaapp.security;

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
	                     AuthenticationException authException) throws IOException, ServletException {
		String error = request.getParameter("error");

		if (error == null)
			log.error("Commence Occurred ::  "+ authException.getMessage());

		JwtErrorCode jwtErrorCode = JwtErrorCode.findByCode(error);

		if (jwtErrorCode == null || jwtErrorCode == JwtErrorCode.JWT_UNKNOWN)
			log.error("Commence Occurred ::  "+ authException.getMessage());

		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		HashMap<String,Object> errorInfo = new HashMap<>();
		errorInfo.put("message", jwtErrorCode.getMessage());
		errorInfo.put("code", jwtErrorCode.getCode());

		String responseJson = objectMapper.writeValueAsString(errorInfo);
		response.getWriter().write(responseJson);
	}
}