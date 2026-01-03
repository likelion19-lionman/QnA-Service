package com.example.springqnaapp.common.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
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
	public ResponseEntity<?> badRequest(MethodArgumentNotValidException e) {
		 Map<String, String> errors = new HashMap<>();
		 e.getBindingResult().getFieldErrors().forEach(error ->
		     errors.put(error.getField(), error.getDefaultMessage())
		 );
		 return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> badRequest(IllegalArgumentException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
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
}