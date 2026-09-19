package com.insurance.service;

@javax.ejb.ApplicationException(rollback = true)
public class InvalidClaimException extends RuntimeException {
    public InvalidClaimException(String message) {
        super(message);
    }
}
