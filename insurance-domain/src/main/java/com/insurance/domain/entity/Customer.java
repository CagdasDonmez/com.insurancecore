package com.insurance.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String customerNumber;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String city;
    private String state;
    private String country;
    private String address;
    private String postalCode;
    @OneToMany(mappedBy = "customer")
    private List<Policy> policies = new ArrayList<>();
    @Version
    private Long version;
}
