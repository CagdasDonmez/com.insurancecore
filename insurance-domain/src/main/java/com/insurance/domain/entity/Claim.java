package com.insurance.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Claim implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String claimNumber;

    @ManyToOne
    private Policy policy;
    private BigDecimal amount;
    private String description;
    private LocalDate creationDate;

    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    private ClaimStatus status;
}
