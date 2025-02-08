package org.gymCrm.hibernate.util;

import org.gymCrm.hibernate.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class UserCredentialsUtilTest {
    private UserCredentialsUtil userCredentialsUtil;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userCredentialsUtil = new UserCredentialsUtil(userRepository);
    }

//    @Test
//    void testGenerateUsername_noCollision() {
//
//        String firstName = "John";
//        String lastName = "Doe";
//
//        String username = UserCredentialsUtil.generateUsername(firstName, lastName);
//
//        assertEquals("John.Doe", username);
//
//    }

    @Test
    void testGenerateUsername_withCollision() {
        String firstName = "John";
        String lastName = "Doe";

        String firstUsername = UserCredentialsUtil.generateUsername(firstName, lastName);
        String secondUsername = UserCredentialsUtil.generateUsername(firstName, lastName);

        assertEquals("John.Doe", firstUsername);
        assertEquals("John.Doe1", secondUsername);
    }

    @Test
    void testGeneratePassword() {
        String password = UserCredentialsUtil.generatePassword();

        assertNotNull(password);
        assertEquals(10, password.length());
        assertTrue(password.matches("[a-zA-Z0-9]*"));
    }
}