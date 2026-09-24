package com.insurance.rest.security;

import com.nimbusds.jwt.JWTClaimsSet;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Provider
public class JwtAuthenticationFilter implements ContainerRequestFilter {

    private final JwtValidator jwtValidator  = new JwtValidator();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorization = requestContext.getHeaderString("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        String token = authorization.substring("Bearer ".length()).trim();
        if (token.isBlank()) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        try {
            JWTClaimsSet jwtClaimsSet = jwtValidator.validate(token);
            Map<String, Object> claims = jwtClaimsSet.getClaims();
            Map<String, Object> realmAccess =
                    (Map<String, Object>) claims.get("realm_access");
            if (realmAccess != null) {
                List<String> roles =
                        (List<String>) realmAccess.get("roles");
                if (roles != null) {
                    if (!roles.contains("POLICY_USER")) {
                        requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
                        return;
                    }
                } else  {
                    requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
                    return;
                }
            } else {
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
                return;
            }

        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }
    }
}
