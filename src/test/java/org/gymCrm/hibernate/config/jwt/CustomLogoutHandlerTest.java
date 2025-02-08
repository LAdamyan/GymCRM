package org.gymCrm.hibernate.config.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomLogoutHandlerTest {

    @InjectMocks
    private CustomLogoutHandler customLogoutHandler;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    private final String token = "test.jwt.token";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogout_WithValidToken() {

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        customLogoutHandler.logout(request, response, authentication);

        verify(jwtUtil).blacklistToken(token);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testLogout_WithoutToken() {

        when(request.getHeader("Authorization")).thenReturn(null);

        customLogoutHandler.logout(request, response, authentication);

        verify(jwtUtil, never()).blacklistToken(any());

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}