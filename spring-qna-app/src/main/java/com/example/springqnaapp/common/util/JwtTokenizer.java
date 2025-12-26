package com.example.springqnaapp.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class JwtTokenizer {
	private final byte[] accessToken;
	private final byte[] refreshToken;

	public final Long accessTokenExpiration;
	public final Long refreshTokenExpiration;

	public JwtTokenizer(
			@Value("${jwt.access-secret-key}")
			final String accessSecret,
			@Value("${jwt.refresh-secret-key}")
			final String refreshSecret,
			@Value("${jwt.access-token-expiration}")
			final String accessTokenExpiration,
			@Value("${jwt.refresh-token-expiration}")
			final String refreshTokenExpiration
	) {
		accessToken = accessSecret.getBytes(StandardCharsets.UTF_8);
		refreshToken = refreshSecret.getBytes(StandardCharsets.UTF_8);

		this.accessTokenExpiration = Long.parseLong(accessTokenExpiration);
		this.refreshTokenExpiration = Long.parseLong(refreshTokenExpiration);
	}

	public String createRefreshToken(
			String username,
			String email,
			Set<String> roles
	) {
		return createToken(this.refreshTokenExpiration,
		                   username,
		                   email,
		                   roles,
		                   this.refreshToken);
	}

	public String createAccessToken(
			String username,
			String email,
			Set<String> roles
	) {
		return createToken(this.accessTokenExpiration,
		                   username,
		                   email,
		                   roles,
		                   this.accessToken);
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
		return parseToken(token, accessToken);
	}

	public Claims parseRefreshToken(String token) {
		return parseToken(token, refreshToken);
	}
}