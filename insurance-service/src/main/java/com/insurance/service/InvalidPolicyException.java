package com.insurance.service;

public class InvalidPolicyException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public InvalidPolicyException() {
    }
    public InvalidPolicyException(String message) {
        super(message);
    }
}
