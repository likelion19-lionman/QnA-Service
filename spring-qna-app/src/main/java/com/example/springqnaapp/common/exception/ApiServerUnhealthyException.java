package com.example.springqnaapp.common.exception;

public class ApiServerUnhealthyException extends RuntimeException {
	public ApiServerUnhealthyException(String message) {
		super(message);
	}
}
