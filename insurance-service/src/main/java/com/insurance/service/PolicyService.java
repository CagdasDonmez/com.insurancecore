package com.insurance.service;

import com.insurance.domain.entity.Customer;
import com.insurance.domain.entity.Policy;
import com.insurance.persistence.repository.PolicyRepository;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Stateless
public class PolicyService {

    @EJB
    private PolicyRepository policyRepository;

    public Policy createPolicy(Policy policy, Long customerId) {
        if (customerId == null) { throw new InvalidPolicyException("Customer ID is required"); }
        policy.setCustomer(policyRepository.findCustomer(customerId)
            .orElseThrow(() -> new InvalidPolicyException("Customer does not exist")));
        return createPolicy(policy);
    }

    public Policy createPolicy (Policy policy) throws InvalidPolicyException {
        Customer customer = policy.getCustomer();

        if (customer == null) {
            // return null;
            throw new InvalidPolicyException("Empty Customer");
        }

        if (policy.getPremium() == null || policy.getPremium().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPolicyException("Premium amount must be greater than zero");
        }

        LocalDate validFrom = policy.getValidFrom();
        LocalDate validTo = policy.getValidTo();

        if (validFrom != null && validTo != null) {
            if (validFrom.isAfter(validTo) || validTo.isEqual(validFrom)) {
                throw new InvalidPolicyException("Validity time must be after start time");
            }
        } else {
            throw new InvalidPolicyException("ValidFrom and/or ValidTo are null");
        }

        if (policy.isCancelled()) {
            throw new InvalidPolicyException("Policy is cancelled");
        }

        policyRepository.persist(policy);
        return policy;
    }

    public List<Policy> getPolicies() {
        return policyRepository.findAll();
    }
}
