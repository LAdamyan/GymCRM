package org.gymCrm.hibernate.config.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class CustomAuthenticationProviderTest {

    @InjectMocks
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private BruteForceProtectionService bruteForceProtectionService;

    @Mock
    private UserDetails userDetails;

    private final String username = "testuser";
    private final String correctPassword = "password";
    private final String incorrectPassword = "wrongPassword";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testAuthenticate_SuccessfulLogin() {
//
//        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
//        when(userDetails.getPassword()).thenReturn(passwordEncoder.encode(correctPassword));
//        when(passwordEncoder.matches(correctPassword, userDetails.getPassword())).thenReturn(true);
//        when(userDetails.getAuthorities()).thenReturn(null); // Provide necessary authorities if needed
//        when(bruteForceProtectionService.isBlocked(username)).thenReturn(false);
//
//
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(username, correctPassword);
//        Authentication result = customAuthenticationProvider.authenticate(authenticationToken);
//
//
//        assertNotNull(result);
//        assertEquals(username, result.getName());
//        verify(bruteForceProtectionService).loginSucceeded(username);
//    }

    @Test
    public void testAuthenticate_FailedLogin_BlockedUser() {

        when(bruteForceProtectionService.isBlocked(username)).thenReturn(true);


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, incorrectPassword);


        assertThrows(LockedException.class, () -> {
            customAuthenticationProvider.authenticate(authenticationToken);
        });

        verify(bruteForceProtectionService, never()).loginSucceeded(any());
    }

//    @Test
//    public void testAuthenticate_FailedLogin_IncorrectPassword() {
//
//        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
//        when(userDetails.getPassword()).thenReturn(passwordEncoder.encode(correctPassword));
//        when(passwordEncoder.matches(eq(incorrectPassword), any())).thenReturn(false);
//        when(bruteForceProtectionService.isBlocked(username)).thenReturn(false);
//
//
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(username, incorrectPassword);
//
//
//        assertThrows(BadCredentialsException.class, () -> {
//            customAuthenticationProvider.authenticate(authenticationToken);
//        });
//
//        verify(bruteForceProtectionService).loginFailed(username);
//        verify(bruteForceProtectionService, never()).loginSucceeded(any());
//    }

    @Test
    public void testSupports() {

        assertTrue(customAuthenticationProvider.supports(UsernamePasswordAuthenticationToken.class));
        assertFalse(customAuthenticationProvider.supports(Object.class));
    }
}