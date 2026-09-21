package com.insurance.service;

@javax.ejb.ApplicationException(rollback = true)
public class InvalidCustomerException extends RuntimeException {
    public InvalidCustomerException() {
    }
    public InvalidCustomerException(String message) {
        super(message);
    }
}
