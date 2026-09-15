package com.insurance.service;

import com.insurance.domain.entity.Claim;
import com.insurance.domain.entity.Policy;
import com.insurance.persistence.repository.ClaimRepository;

import javax.ejb.EJB;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ClaimService {

    @EJB
    private ClaimRepository claimRepository;

    public Claim createClaim(Claim claim) throws InvalidClaimException {
        Policy policy = claim.getPolicy();

        if (policy == null) {
            throw new InvalidClaimException("Policy can not be found for the claim");
        }

        BigDecimal claimAmount = claim.getAmount();
        if (claimAmount == null || claimAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidClaimException("Claim amount must be greater than zero");
        }

        LocalDate creationDate = claim.getCreationDate();
        if (creationDate == null) {
            throw new InvalidClaimException("Creation Date must not be null");
        }

        claimRepository.persist(claim);
        return claim;
    }

    public List<Claim> getClaims() {
        return claimRepository.findAll();
    }
}
