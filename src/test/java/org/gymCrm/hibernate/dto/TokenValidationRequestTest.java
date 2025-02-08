package org.gymCrm.hibernate.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenValidationRequestTest {
    @Test
    public void testTokenValidationRequestConstructorAndGetters() {

        String sampleUsername = "testUser";
        String sampleToken = "sample_token_123";
        TokenValidationRequest tokenValidationRequest = new TokenValidationRequest();
        tokenValidationRequest.setUsername(sampleUsername);
        tokenValidationRequest.setToken(sampleToken);


        assertEquals(sampleUsername, tokenValidationRequest.getUsername(), "Username should be correctly set via setter");
        assertEquals(sampleToken, tokenValidationRequest.getToken(), "Token should be correctly set via setter");

    }
}