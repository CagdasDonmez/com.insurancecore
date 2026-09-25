package com.insurance.service;

import com.insurance.domain.entity.Customer;
import com.insurance.domain.entity.Policy;
import com.insurance.persistence.repository.CustomerRepository;
import com.insurance.persistence.repository.PolicyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PolicyServiceTest {

    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private PolicyService policyService;

    @Test
    public void createPolicyTest() {
        Policy policy = new Policy();
        policy.setPolicyNumber("POL-001");
        policy.setPremium(new BigDecimal("100.0"));
        policy.setValidFrom(LocalDate.now());
        policy.setValidTo(LocalDate.now().plusYears(1));

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCustomerNumber("CUST-001");
        when(customerRepository.findCustomerByCustomerNumber("CUST-001")).thenReturn(Optional.of(customer));

        Policy result = policyService.createPolicy(policy, "CUST-001");

        assertSame(policy, result);
        assertSame(customer, policy.getCustomer());

        verify(policyRepository, times(1)).persist(policy);
    }

    @Test
    public void customerNotFound() {
        Policy policy = new Policy();
        policy.setPolicyNumber("POL-001");
        policy.setPremium(new BigDecimal("100.0"));
        policy.setValidFrom(LocalDate.now());
        policy.setValidTo(LocalDate.now().plusYears(1));

        when(customerRepository.findCustomerByCustomerNumber("CUST-100")).thenReturn(Optional.empty());
        assertThrows(InvalidPolicyException.class, () -> {
            policyService.createPolicy(policy, "CUST-100");
        });

        verify(policyRepository, never()).persist(policy);
    }

    @Test
    public void invalidPremium() {
        Policy policy = new Policy();
        policy.setPolicyNumber("POL-001");
        policy.setPremium(new BigDecimal("-100.0"));
        policy.setValidFrom(LocalDate.now());
        policy.setValidTo(LocalDate.now().plusYears(1));

        Customer customer = new Customer();
        customer.setCustomerNumber("CUST-001");

        when(customerRepository.findCustomerByCustomerNumber("CUST-001")).thenReturn(Optional.of(customer));
        assertThrows(InvalidPolicyException.class, () -> {
            policyService.createPolicy(policy, "CUST-001");
        });

        verify(policyRepository, never()).persist(policy);
    }

    @Test
    public void invalidValidity() {
        Policy policy = new Policy();
        policy.setPolicyNumber("POL-001");
        policy.setPremium(new BigDecimal("100.0"));
        policy.setValidFrom(LocalDate.now());
        policy.setValidTo(LocalDate.now().minusDays(2));

        Customer customer = new Customer();
        customer.setCustomerNumber("CUST-001");
        when(customerRepository.findCustomerByCustomerNumber("CUST-001")).thenReturn(Optional.of(customer));
        Exception exception = assertThrows(InvalidPolicyException.class, () -> {
            policyService.createPolicy(policy, "CUST-001");
        });
        assertEquals("Validity time must be after start time", exception.getMessage());
        verify(policyRepository, never()).persist(policy);

        policy.setValidFrom(null);
        exception = assertThrows(InvalidPolicyException.class, () -> {
            policyService.createPolicy(policy, "CUST-001");
        });
        assertEquals("ValidFrom and/or ValidTo are null", exception.getMessage());
        verify(policyRepository, never()).persist(policy);
    }

    @Test
    public void policyExists() {
        Policy policy = new Policy();
        policy.setPolicyNumber("POL-001");
        policy.setId(1L);

        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));
        Policy result = policyService.getPolicy(1L);

        assertEquals(policy, result);
        verify(policyRepository, times(1)).findById(1L);
    }

    @Test
    public void policyNotFound() {
        when(policyRepository.findById(999L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(PolicyNotFoundException.class, () -> {
            policyService.getPolicy(999L);
        });
        assertEquals("Policy does not exist", exception.getMessage());
        verify(policyRepository, times(1)).findById(999L);
    }

    @Test
    public void captureArgument() {
        Customer customer = new Customer();
        customer.setCustomerNumber("CUST-001");

        Policy policy = new Policy();
        policy.setPolicyNumber("POL-777");
        policy.setPremium(new BigDecimal("250.0"));

        LocalDate validFrom = LocalDate.now();
        policy.setValidFrom(validFrom);
        LocalDate validTo = LocalDate.now().plusYears(1);
        policy.setValidTo(validTo);

        when(customerRepository.findCustomerByCustomerNumber("CUST-001")).thenReturn(Optional.of(customer));
        policyService.createPolicy(policy, "CUST-001");
        ArgumentCaptor<Policy> captor = ArgumentCaptor.forClass(Policy.class);
        verify(policyRepository).persist(captor.capture());
        Policy captured_policy = captor.getValue();

        assertEquals(policy, captured_policy);
        assertEquals("POL-777", captured_policy.getPolicyNumber());
        assertEquals(new BigDecimal("250.0"), captured_policy.getPremium());
        assertEquals(validFrom, captured_policy.getValidFrom());
        assertEquals(validTo, captured_policy.getValidTo());
    }
}
