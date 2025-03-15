package org.gymCrm.hibernate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gymCrm.hibernate.config.jwt.JwtUtil;
import org.gymCrm.hibernate.dto.LoginRequest;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.service.impl.UserServiceImpl;
import org.gymCrm.hibernate.util.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
class LoginControllerTest {


        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private AuthenticationService authenticationService;

        @MockBean
        private AuthenticationManager authenticationManager;

        @MockBean
        private JwtUtil jwtUtil;

        @MockBean
        private UserServiceImpl userServiceImpl;

        @Autowired
        private ObjectMapper objectMapper;

        @BeforeEach
        public void setup() {
            // Set up mock behavior before each test
        }

    @Test
    void login_Success() throws Exception {
        String username = "testuser";
        String password = "password";
        String token = "mockJwtToken";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(jwtUtil.generateToken(eq(username), anyList())).thenReturn(token);

        mockMvc.perform(post("/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, times(1)).generateToken(eq(username), anyList());
    }

    @Test
    void login_Failure_InvalidCredentials() throws Exception {
        String username = "testuser";
        String password = "wrongpassword";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        mockMvc.perform(post("/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }

    @Test
    void changePassword_Success() throws Exception {
        String username = "testuser";
        String oldPassword = "oldpassword";
        String newPassword = "newpassword";
        Trainer trainer = new Trainer();


        when(authenticationService.authenticate(username, oldPassword)).thenReturn(true);
        when(userServiceImpl.findByUsername(username)).thenReturn(Optional.of(trainer));

        mockMvc.perform(put("/change-password")
                        .param("username", username)
                        .param("oldPassword", oldPassword)
                        .param("newPassword", newPassword))
                .andExpect(status().isOk())
                .andExpect(content().string("Password changed successfully"));

        verify(authenticationService, times(1)).authenticate(username, oldPassword);
        verify(userServiceImpl, times(1)).changePassword(username, oldPassword, newPassword);
    }

    @Test
    void changePassword_Failure_InvalidOldPassword() throws Exception {
        String username = "testuser";
        String oldPassword = "wrongOldPassword";
        String newPassword = "newpassword";

        when(authenticationService.authenticate(username, oldPassword)).thenReturn(false);

        mockMvc.perform(put("/change-password")
                        .param("username", username)
                        .param("oldPassword", oldPassword)
                        .param("newPassword", newPassword))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid old password"));

        verify(authenticationService, times(1)).authenticate(username, oldPassword);
    }

    @Test
    void changePassword_Failure_UserNotFound() throws Exception {
        String username = "testuser";
        String oldPassword = "oldpassword";
        String newPassword = "newpassword";

        when(authenticationService.authenticate(username, oldPassword)).thenReturn(true);
        when(userServiceImpl.findByUsername(username)).thenReturn(Optional.empty());

        mockMvc.perform(put("/change-password")
                        .param("username", username)
                        .param("oldPassword", oldPassword)
                        .param("newPassword", newPassword))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(authenticationService, times(1)).authenticate(username, oldPassword);
    }
}