package org.gymCrm.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserCredentialsUtilTest {

    @BeforeEach
    public void setUp() {
        UserCredentialsUtil.clearUsernameCounts();
    }

    @Test
    public void testGenerateUsername_FirstTime_ShouldReturnUsernameWithoutNumber() {
        String firstName = "John";
        String lastName = "Doe";
        String expectedUsername = "John.Doe";

        String username = UserCredentialsUtil.generateUsername(firstName, lastName);

        assertEquals(expectedUsername, username, "The username should match the expected format without numeric suffix.");
    }

    @Test
    public void testGenerateUsername_SecondTime_ShouldReturnUsernameWithNumber() {
        String firstName = "John";
        String lastName = "Doe";
        String expectedFirstUsername = "John.Doe";
        String expectedSecondUsername = "John.Doe1";

        String firstUsername = UserCredentialsUtil.generateUsername(firstName, lastName);
        String secondUsername = UserCredentialsUtil.generateUsername(firstName, lastName);

        assertEquals(expectedFirstUsername, firstUsername, "First generated username did not match expected.");
        assertEquals(expectedSecondUsername, secondUsername, "Second generated username did not match expected.");
    }

    @Test
    public void testGeneratePassword_ShouldReturnCorrectLengthAndRandomness() {
        int passwordLength = 10;
        String password = UserCredentialsUtil.generatePassword();

        assertEquals(passwordLength, password.length(), "Password length should be " + passwordLength);
        String anotherPassword = UserCredentialsUtil.generatePassword();
        assertNotEquals(password, anotherPassword, "Two successive passwords should not be the same.");
    }

}