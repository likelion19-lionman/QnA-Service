package com.example.springqnaapp.common.util;

import com.example.springqnaapp.config.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Set;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties({ JwtProperties.class })
public class JwtTokenizer {
	private final JwtProperties jwtProperties;

	public String createRefreshToken(
			String username,
			String email,
			Set<String> roles
	) {
		return createToken(jwtProperties.getRefreshTokenExpiration(),
		                   username,
		                   email,
		                   roles,
		                   jwtProperties.getRefreshSecretKey());
	}

	public String createAccessToken(
			String username,
			String email,
			Set<String> roles
	) {
		return createToken(jwtProperties.getAccessTokenExpiration(),
		                   username,
		                   email,
		                   roles,
		                   jwtProperties.getAccessSecretKey());
	}

	private String createToken(
			Long expiration,
			String username,
			String email,
			Set<String> roles,
			byte[] secret
	) {
		Date now = new Date();
		Date expiredDate = new Date(now.getTime() + expiration);

		return Jwts.builder()
		           .claim("username", username)
		           .claim("email", email)
		           .claim("roles", roles)
		           .issuedAt(now)
		           .expiration(expiredDate)
		           .signWith(getKey(secret))
		           .compact();
	}

	private SecretKey getKey(byte[] secret) {
		return Keys.hmacShaKeyFor(secret);
	}


	private Claims parseToken(String token, byte[] secret) {
		return Jwts.parser()
		           .verifyWith(getKey(secret))
		           .build()
		           .parseSignedClaims(token)
		           .getPayload();
	}

	public Claims parseAccessToken(String token) {
		return parseToken(token, jwtProperties.getAccessSecretKey());
	}

	public Claims parseRefreshToken(String token) {
		return parseToken(token, jwtProperties.getRefreshSecretKey());
	}
}