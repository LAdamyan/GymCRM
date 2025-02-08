package org.gymCrm.hibernate.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {
    @Test
    public void testLoginRequestConstructorAndGetters() {

        String sampleUsername = "testUser";
        String samplePassword = "testPassword";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(sampleUsername);
        loginRequest.setPassword(samplePassword);

        assertEquals(sampleUsername, loginRequest.getUsername(), "Username should be correctly set via setter");
        assertEquals(samplePassword, loginRequest.getPassword(), "Password should be correctly set via setter");
    }
}