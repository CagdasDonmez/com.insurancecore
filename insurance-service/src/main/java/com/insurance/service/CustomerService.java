package com.insurance.service;

import com.insurance.domain.entity.Customer;
import com.insurance.persistence.repository.CustomerRepository;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Optional;

@Stateless
public class CustomerService {

    @EJB
    private CustomerRepository customerRepository;

    public Customer findCustomer(String customerNumber) {
        Optional<Customer> customerByCustomerNumber = customerRepository.findCustomerByCustomerNumber(customerNumber);
        if (customerByCustomerNumber.isPresent()) {
            return customerByCustomerNumber.get();
        } else
            throw new InvalidCustomerException(customerNumber);
    }
}
