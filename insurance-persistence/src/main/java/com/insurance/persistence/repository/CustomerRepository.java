package com.insurance.persistence.repository;

import com.insurance.domain.entity.Customer;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Stateless
public class CustomerRepository {

    @PersistenceContext(unitName = "insurancePU")
    private EntityManager entityManager;

    public Optional<Customer> findCustomerById(Long id) {
        return Optional.ofNullable(entityManager.find(Customer.class, id));
    }

    public Optional<Customer> findCustomerByName(String name) {
        return Optional.ofNullable(entityManager.find(Customer.class, name));
    }

    public Optional<Customer> findCustomerByCustomerNumber(String customerNumber) {
        TypedQuery<Customer> query = entityManager.createQuery("select c from Customer c where c.customerNumber = :customerNumber", Customer.class)
                .setParameter("customerNumber", customerNumber);
        return query.getResultList().stream().findFirst();
    }

    public List<Customer> findAllCustomers() {
        TypedQuery<Customer> selectCFromCustomerC = entityManager.createQuery("select c from Customer c", Customer.class);
        return selectCFromCustomerC.getResultList();
    }

    public void persist(Customer customer) {
        entityManager.persist(customer);
    }
}
