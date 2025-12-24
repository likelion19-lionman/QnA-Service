package com.example.springqnaapp.security.jwt;

import lombok.Getter;

@Getter
public enum JwtErrorCode {
	JWT_TOKEN_EXPIRED("J001", "토큰이 만료되었습니다"),
	JWT_INVALID_SIGNATURE("J002", "유효하지 않은 서명입니다"),
	JWT_MALFORMED("J003", "잘못된 JWT 형식입니다"),
	JWT_UNSUPPORTED("J004", "지원하지 않는 JWT 토큰입니다"),
	JWT_ILLEGAL_ARGUMENT("J005", "JWT 토큰이 잘못되었습니다"),
	JWT_NOT_YET_VALID("J006", "아직 유효하지 않은 토큰입니다"),
	JWT_UNKNOWN("J999", "감지하지 못한 오류입니다.");

	private final String code;
	private final String message;

	JwtErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public static JwtErrorCode findByCode(String code) {
		for (JwtErrorCode errorCode : values())
			if (errorCode.code.equals(code))
				return errorCode;
		return JWT_UNKNOWN;
	}
}