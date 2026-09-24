package com.insurance.rest.security;

import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.proc.JWSAlgorithmFamilyJWSKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

public class JwtValidator {

    private static final String ISSUER =
            "http://localhost:8080/realms/insurance";

    private static final String JWKS_URL =
            "http://localhost:8080/realms/insurance/protocol/openid-connect/certs";

    private final ConfigurableJWTProcessor<SecurityContext> jwtProcessor;

    public JwtValidator() {
        try {
            jwtProcessor = new DefaultJWTProcessor<>();

            jwtProcessor.setJWSKeySelector(
                    JWSAlgorithmFamilyJWSKeySelector.fromJWKSetURL(
                            new URL(JWKS_URL)
                    )
            );

            JWTClaimsSet expectedClaims =
                    new JWTClaimsSet.Builder()
                            .issuer(ISSUER)
                            .build();

            jwtProcessor.setJWTClaimsSetVerifier(
                    new DefaultJWTClaimsVerifier<>(
                            expectedClaims,
                            Set.of("sub", "exp")
                    )
            );

        } catch (MalformedURLException | KeySourceException e) {
            throw new IllegalStateException("Invalid JWKS URL", e);
        }
    }

    public JWTClaimsSet validate(String token) throws Exception {
        return jwtProcessor.process(token, null);
    }
}