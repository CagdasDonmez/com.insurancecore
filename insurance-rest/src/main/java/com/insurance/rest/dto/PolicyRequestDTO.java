package com.insurance.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PolicyRequestDTO {

    private String policyNumber;
    private String customerNumber;
    private LocalDate validFrom;
    private LocalDate validTo;
    private BigDecimal premium;

    public PolicyRequestDTO() {}

    public String getPolicyNumber() {
        return policyNumber;
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }
}


