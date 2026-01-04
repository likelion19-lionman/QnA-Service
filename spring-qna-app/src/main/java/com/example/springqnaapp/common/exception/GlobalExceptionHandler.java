package com.example.springqnaapp.common.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	// 명확하게 반환 타입을 Map으로 지정하거나, 별도의 에러 DTO를 만드세요.
	public ResponseEntity<Map<String, String>> badRequest(MethodArgumentNotValidException e) {
		Map<String, String> errors = new HashMap<>();

		e.getBindingResult().getFieldErrors().forEach(
				error -> errors.put(error.getField(), error.getDefaultMessage())
		);

		// 명시적으로 JSON 응답임을 보장
		return ResponseEntity
				.badRequest()
				.contentType(MediaType.APPLICATION_JSON)
				.body(errors);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> badRequest(IllegalArgumentException e) {
		return ResponseEntity.badRequest()
		                     .contentType(MediaType.APPLICATION_JSON)
				             .body(Map.of("detail", e.getMessage()));
	}

	@ExceptionHandler({
			JwtException.class,
			UnauthorizedException.class
	})
	public ErrorResponse unauthorized(Exception e) {
		return ErrorResponse.builder(
				e,
				HttpStatus.UNAUTHORIZED,
				e.getMessage()
		).build();
	}

	@ExceptionHandler({
			ApiServerUnhealthyException.class
	})
	public ErrorResponse serviceUnavailable(Exception e) {
		return ErrorResponse.builder(
				e,
				HttpStatus.SERVICE_UNAVAILABLE,
				e.getMessage()
		).build();
	}

	@ExceptionHandler({
			EmailCodeExpiredException.class
	})
	public ErrorResponse gone(Exception e) {
		return ErrorResponse.builder(
				e,
				HttpStatus.GONE,
				e.getMessage()
		).build();
	}

	@ExceptionHandler({
			NotVerifiedException.class
	})
	public ErrorResponse forbidden(Exception e) {
		return ErrorResponse.builder(
				e,
				HttpStatus.UNAUTHORIZED,
				e.getMessage()
		).build();
	}

	@ExceptionHandler({
			UserNotFoundException.class,
			QnaNotFoundException.class
	})
	public ErrorResponse notFound(Exception e) {
		return ErrorResponse.builder(
				e,
				HttpStatus.NOT_FOUND,
				e.getMessage()
		).build();
	}

	@ExceptionHandler({
			UserExistedException.class
	})
	public ErrorResponse conflict(Exception e) {
		return ErrorResponse.builder(
				e,
				HttpStatus.CONFLICT,
				e.getMessage()
		).build();
	}

	@ExceptionHandler({
			QnaNotCommentableException.class
	})
	public ErrorResponse preconditionFailed(Exception e) {
		return ErrorResponse.builder(
				e,
				HttpStatus.PRECONDITION_FAILED,
				e.getMessage()
		).build();
	}
}