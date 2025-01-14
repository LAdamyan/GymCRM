package org.gymCrm.hibernate.service.impl;

import org.gymCrm.hibernate.dao.UserDAO;
import org.gymCrm.hibernate.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserDAO<User> userDAO;

    @InjectMocks
    private UserDetailsServiceImpl<User> userDetailsService;

    @Test
    void authenticate_SuccessfulAuthentication() {
        String username = "john.doe";
        String password = "password123";

        User user = mock(User.class);
        when(user.getPassword()).thenReturn(password);

        when(userDAO.findByUsername(username)).thenReturn(Optional.of(user));

        boolean result = userDetailsService.authenticate(username, password);

        assertTrue(result, "Authentication should succeed with correct username and password.");
        verify(userDAO, times(1)).findByUsername(username);
        verify(user, times(1)).getPassword();


    }

    @Test
    void authenticate_UserNotFound() {
        String username = "nonexistent.user";
        String password = "password123";

        when(userDAO.findByUsername(username)).thenReturn(Optional.empty());

        boolean result = userDetailsService.authenticate(username, password);

        assertFalse(result, "Authentication should fail if the user does not exist.");
        verify(userDAO, times(1)).findByUsername(username);
    }

}