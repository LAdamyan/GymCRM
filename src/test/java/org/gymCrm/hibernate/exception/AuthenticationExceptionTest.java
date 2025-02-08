package org.gymCrm.hibernate.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationExceptionTest {

    @Test
    void testExceptionMessage() {
        String errorMessage = "Authentication failed";
        AuthenticationException exception = new AuthenticationException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }

}