package com.insurance.rest.dto;

public class CreatePolicyResponseDTO {

    private String policyNumber;
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getMessage() {
        return message;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }
}
