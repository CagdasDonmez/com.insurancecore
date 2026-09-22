package com.insurance.rest.exception;

import com.insurance.rest.dto.ErrorResponseDTO;
import com.insurance.service.InvalidPolicyException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.time.LocalDateTime;

@Provider
public class InvalidPolicyExceptionManager implements ExceptionMapper<InvalidPolicyException> {

    @Override
    public Response toResponse(InvalidPolicyException exception) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setMessage(exception.getMessage());
        errorResponseDTO.setTimestamp(LocalDateTime.now());
        errorResponseDTO.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
        errorResponseDTO.setError(Response.Status.BAD_REQUEST.getReasonPhrase());

        return Response.status(Response.Status.BAD_REQUEST).entity(errorResponseDTO).build();
    }
}
