package com.example.springqnaapp.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieHandler {
	private static final boolean XSS_ATTACK_SECURE = true;
	private static final boolean ONLY_HTTPS = false;
	private static final String VALID_PARENT_PATH = "/";
	private static final int MAX_AGE_SECONDS = 30 * 60;

	public void createCookie(
			HttpServletResponse response,
			String key,
			String value) {
		Cookie cookie = new Cookie(key, value);

		cookie.setHttpOnly(XSS_ATTACK_SECURE);
		cookie.setSecure(ONLY_HTTPS);
		cookie.setPath(VALID_PARENT_PATH);
		cookie.setMaxAge(MAX_AGE_SECONDS);

		response.addCookie(cookie);
	}

    public void deleteCookie(HttpServletResponse response, String key){
        Cookie cookie = new Cookie(key,null);

        cookie.setHttpOnly(XSS_ATTACK_SECURE);
        cookie.setSecure(ONLY_HTTPS);
        cookie.setPath(VALID_PARENT_PATH);
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }
}