package com.insurance.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Policy {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String policyNumber;

    @ManyToOne
    private Customer customer;
    private LocalDate validFrom;
    private LocalDate validTo;
    private boolean cancelled;
    @OneToMany
    private Claim claim;
    private BigDecimal premium;
    @Version
    private Long version;

    public PolicyStatus getStatus() {
        LocalDate now = LocalDate.now();
        if (cancelled) {
            return PolicyStatus.CANCELLED;
        }

        if (now.isAfter(validTo)) {
            return PolicyStatus.EXPIRED;
        } else if (now.isBefore(validFrom)) {
            return PolicyStatus.SCHEDULED;
        }

        return PolicyStatus.ACTIVE;
    }

}