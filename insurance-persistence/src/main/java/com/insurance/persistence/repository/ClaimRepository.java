package com.insurance.persistence.repository;

import com.insurance.domain.entity.Claim;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Stateless
public class ClaimRepository {

    @PersistenceContext(unitName = "insurancePU")
    private EntityManager entityManager;

    public List<Claim> findAll() {
        TypedQuery<Claim> selectCFromClaimC = entityManager.createQuery("Select c from Claim c", Claim.class);
        return selectCFromClaimC.getResultList();
    }

    public Optional<Claim> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Claim.class, id));
    }

    public void persist (Claim claim) {
        entityManager.persist(claim);
    }

}
