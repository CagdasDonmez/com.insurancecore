package com.insurance.service;

@javax.ejb.ApplicationException(rollback = true)
public class InvalidPolicyException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public InvalidPolicyException() {
    }
    public InvalidPolicyException(String message) {
        super(message);
    }
}
