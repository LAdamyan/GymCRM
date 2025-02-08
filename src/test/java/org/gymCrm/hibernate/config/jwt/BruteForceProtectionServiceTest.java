package org.gymCrm.hibernate.config.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BruteForceProtectionServiceTest {

    private BruteForceProtectionService bruteForceProtectionService;

    @BeforeEach
    public void setUp() {
        bruteForceProtectionService = new BruteForceProtectionService();
    }

    @Test
    public void testLoginFailed_IncrementsAttempts() {
        String username = "testuser";


        bruteForceProtectionService.loginFailed(username);
        bruteForceProtectionService.loginFailed(username);
        bruteForceProtectionService.loginFailed(username);

        assertTrue(bruteForceProtectionService.isBlocked(username));

        bruteForceProtectionService.loginFailed(username);

        assertTrue(bruteForceProtectionService.isBlocked(username));
    }

    @Test
    public void testIsBlocked_UserNotBlocked() {
        String username = "testuser";
        assertFalse(bruteForceProtectionService.isBlocked(username));
    }

//    @Test
//    public void testIsBlocked_UserBlocked_Expiration() throws InterruptedException {
//        String username = "testuser";
//
//        // Simulate failed login attempts to block the user
//        bruteForceProtectionService.loginFailed(username);
//        bruteForceProtectionService.loginFailed(username);
//        bruteForceProtectionService.loginFailed(username);
//
//        // Check that the user is initially blocked
//        assertTrue(bruteForceProtectionService.isBlocked(username));
//
//        // Simulate waiting for lock time duration to expire
//        Thread.sleep(1_000 * 6); // Wait for more than 5 seconds
//
//        // Check that the block has expired
//        assertFalse(bruteForceProtectionService.isBlocked(username));
//    }

  //  @Test
//    public void testLoginSucceeded_RemovesAttempts() {
//        String username = "testuser";
//
//        // Simulate failed login attempts to block the user
//        bruteForceProtectionService.loginFailed(username);
//        bruteForceProtectionService.loginFailed(username);
//        bruteForceProtectionService.loginFailed(username);
//
//        // Check that the user is blocked
//        assertTrue(bruteForceProtectionService.isBlocked(username));
//
//        // Simulate successful login
//        bruteForceProtectionService.loginSucceeded(username);
//
//        // Verify that the user is not blocked anymore
//        assertFalse(bruteForceProtectionService.isBlocked(username));
//    }


}