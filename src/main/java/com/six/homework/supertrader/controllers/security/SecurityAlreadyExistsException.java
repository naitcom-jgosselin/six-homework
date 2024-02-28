package com.six.homework.supertrader.controllers.security;

public class SecurityAlreadyExistsException extends RuntimeException {
    public SecurityAlreadyExistsException(String message) {
        super(message);
    }
}
