package com.insurance.rest.resource;

import com.insurance.domain.entity.Policy;
import com.insurance.rest.dto.CreatePolicyResponseDTO;
import com.insurance.rest.dto.PolicyRequestDTO;
import com.insurance.rest.dto.PolicyResponseDTO;
import com.insurance.service.PolicyService;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Path("/policies")
@Produces(MediaType.APPLICATION_JSON)
public class PolicyResource {

    @EJB
    private PolicyService policyService;

    @GET
    public List<PolicyResponseDTO> getPolicies() {
        List<Policy> policies = policyService.getPolicies();
        List<PolicyResponseDTO> policiesDTO = new ArrayList<>();

        for (Policy policy : policies) {
            PolicyResponseDTO policyResponseDTO = createPolicyResponseDTO(policy);
            policiesDTO.add(policyResponseDTO);
        }

        return policiesDTO;
    }

    @GET
    @Path("/{id}")
    public PolicyResponseDTO getPolicyById(@PathParam("id") Long id) {
        Policy policy = policyService.getPolicy(id);
        return createPolicyResponseDTO(policy);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPolicy(PolicyRequestDTO policyRequestDTO) {
        String customerNumber = policyRequestDTO.getCustomerNumber();
        String policyNumber = policyRequestDTO.getPolicyNumber();
        BigDecimal premium = policyRequestDTO.getPremium();
        LocalDate validFrom = policyRequestDTO.getValidFrom();
        LocalDate validTo = policyRequestDTO.getValidTo();

        Policy policy = new Policy();
        policy.setPolicyNumber(policyNumber);
        policy.setPremium(premium);
        policy.setValidFrom(validFrom);
        policy.setValidTo(validTo);
        policyService.createPolicy(policy, customerNumber);

        CreatePolicyResponseDTO createPolicyResponseDTO = new CreatePolicyResponseDTO();
        createPolicyResponseDTO.setMessage("Success");
        createPolicyResponseDTO.setPolicyNumber(policyNumber);
        return Response.status(Response.Status.CREATED).entity(createPolicyResponseDTO).build();
    }

    private PolicyResponseDTO createPolicyResponseDTO(Policy policy) {
        PolicyResponseDTO policyResponseDTO = new PolicyResponseDTO();
        policyResponseDTO.setCustomerNumber(policy.getCustomer().getCustomerNumber());
        policyResponseDTO.setPolicyNumber(policy.getPolicyNumber());
        policyResponseDTO.setStatus(policy.getStatus().name());
        policyResponseDTO.setValidFrom(policy.getValidFrom());
        policyResponseDTO.setValidTo(policy.getValidTo());
        policyResponseDTO.setPremium(policy.getPremium());
        return policyResponseDTO;
    }
}
