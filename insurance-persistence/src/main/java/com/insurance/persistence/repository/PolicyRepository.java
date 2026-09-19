package com.insurance.persistence.repository;

import com.insurance.domain.entity.Policy;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;

@Stateless
public class PolicyRepository {

    @PersistenceContext(unitName = "insurancePU")
    private EntityManager entityManager;

    public Optional<com.insurance.domain.entity.Customer> findCustomer(Long id) {
        return Optional.ofNullable(entityManager.find(com.insurance.domain.entity.Customer.class, id));
    }

    public Optional<Policy> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Policy.class, id));
    }

    public List<Policy> findAll() {
        TypedQuery<Policy> selectPFromPolicyP = entityManager.createQuery("select p from Policy p", Policy.class);
        return selectPFromPolicyP.getResultList();
    }

    public void persist(Policy policy) {
        entityManager.persist(policy);
    }
}
