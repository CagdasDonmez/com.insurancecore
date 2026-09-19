package com.insurance.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Policy implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

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
    @OneToMany(mappedBy = "policy")
    private List<Claim> claims = new ArrayList<>();
    private BigDecimal premium;
    @Version
    private Long version;

    public PolicyStatus getStatus() {
        LocalDate now = LocalDate.now();
        if (cancelled) {
            return PolicyStatus.CANCELLED;
        }

        if (validFrom == null || validTo == null) { return PolicyStatus.PENDING; }
        if (now.isAfter(validTo)) {
            return PolicyStatus.EXPIRED;
        } else if (now.isBefore(validFrom)) {
            return PolicyStatus.SCHEDULED;
        }

        return PolicyStatus.ACTIVE;
    }

}