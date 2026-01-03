package com.example.springqnaapp.common.exception;

public class UserExistedException extends RuntimeException {
	public UserExistedException(String message) {
		super(message);
	}
}
