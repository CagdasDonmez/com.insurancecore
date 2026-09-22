package com.insurance.rest.exception;

import com.insurance.rest.dto.ErrorResponseDTO;
import com.insurance.service.PolicyNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.time.LocalDateTime;

@Provider
public class PolicyNotFoundExceptionManager implements ExceptionMapper<PolicyNotFoundException> {

    @Override
    public Response toResponse(PolicyNotFoundException exception) {
        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.NOT_FOUND);

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setMessage(exception.getMessage());
        errorResponseDTO.setStatus(Response.Status.NOT_FOUND.getStatusCode());
        errorResponseDTO.setTimestamp(LocalDateTime.now());
        errorResponseDTO.setError(Response.Status.NOT_FOUND.getReasonPhrase());

        return responseBuilder.entity(errorResponseDTO).build();
    }
}
