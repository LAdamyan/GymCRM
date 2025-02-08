package org.gymCrm.hibernate.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    @Test
    public void testAuthResponseConstructorAndGetter() {

        String sampleToken = "sample_token_123";
        AuthResponse authResponse = new AuthResponse(sampleToken);

        assertEquals(sampleToken, authResponse.getToken(), "Token should be correctly set via constructor");
    }
}