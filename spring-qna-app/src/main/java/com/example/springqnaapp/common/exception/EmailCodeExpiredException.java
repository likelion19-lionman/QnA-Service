package com.example.springqnaapp.common.exception;

public class EmailCodeExpiredException extends RuntimeException {
    public EmailCodeExpiredException(String message) {
        super(message);
    }
}
