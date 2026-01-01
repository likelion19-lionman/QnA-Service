package com.example.springqnaapp.security.transform;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class CookieToAuthHeaderFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain
	) throws ServletException, IOException {
		String accessToken = getAccessTokenFromCookies(request);

		if (accessToken == null) {
			filterChain.doFilter(request, response);
			return;
		}

		CookieToAuthorizationRequestWrapper requestWrapper
				= new CookieToAuthorizationRequestWrapper(request, accessToken);

		filterChain.doFilter(requestWrapper, response);
	}

	private String getAccessTokenFromCookies(HttpServletRequest request) {
		if (request.getCookies() == null) return null;
		return Arrays.stream(request.getCookies())
		             .filter(cookie -> "accessToken".equals(cookie.getName()))
		             .map(Cookie::getValue)
		             .findFirst()
		             .orElse(null);
	}
}