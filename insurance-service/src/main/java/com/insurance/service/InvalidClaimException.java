package com.insurance.service;

public class InvalidClaimException extends RuntimeException {
    public InvalidClaimException(String message) {
        super(message);
    }
}
