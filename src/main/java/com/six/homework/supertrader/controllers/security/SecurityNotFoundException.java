package com.six.homework.supertrader.controllers.security;

public class SecurityNotFoundException extends RuntimeException {
    SecurityNotFoundException(String message) {
        super(message);
    }
}
