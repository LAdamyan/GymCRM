package org.gymCrm.hibernate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gymCrm.hibernate.config.jwt.JwtUtil;
import org.gymCrm.hibernate.dto.LoginRequest;
import org.gymCrm.hibernate.service.impl.UserServiceImpl;
import org.gymCrm.hibernate.util.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
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
        public void testLogin_Success() throws Exception {
            // Mock the authentication service
            String username = "testuser";
            String password = "password";
            String token = "mockJwtToken";
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername(username);
            loginRequest.setPassword(password);

            // Mock behavior for authentication
            when(authenticationService.authenticate(username, password)).thenReturn(true);
            when(jwtUtil.generateToken(username, List.of("USER"))).thenReturn(token);

            // Perform POST request to /login
            mockMvc.perform(post("/login")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value(token));

            verify(authenticationService, times(1)).authenticate(username, password);
            verify(jwtUtil, times(1)).generateToken(username, List.of("USER"));
        }

        @Test
        public void testLogin_Failure_InvalidCredentials() throws Exception {
            // Mock the authentication service for failed login
            String username = "testuser";
            String password = "wrongpassword";
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername(username);
            loginRequest.setPassword(password);

            when(authenticationService.authenticate(username, password)).thenReturn(false);

            // Perform POST request to /login
            mockMvc.perform(post("/login")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string("Invalid username or password"));
        }


        @Test
        public void testChangePassword_Failure_InvalidOldPassword() throws Exception {
            // Prepare input
            String username = "testuser";
            String oldPassword = "wrongOldPassword";
            String newPassword = "newpassword";

            // Mock behavior for failed authentication
            when(authenticationService.authenticate(username, oldPassword)).thenReturn(false);

            // Perform PUT request to change password
            mockMvc.perform(put("/change-password")
                            .param("username", username)
                            .param("oldPassword", oldPassword)
                            .param("newPassword", newPassword))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string("Invalid old password"));

            verify(authenticationService, times(1)).authenticate(username, oldPassword);
        }

        @Test
        public void testChangePassword_Failure_UserNotFound() throws Exception {
            // Prepare input
            String username = "testuser";
            String oldPassword = "oldpassword";
            String newPassword = "newpassword";

            // Mock behavior for user not found
            when(authenticationService.authenticate(username, oldPassword)).thenReturn(true);
            when(userServiceImpl.findByUsername(username)).thenReturn(Optional.empty());

            // Perform PUT request to change password
            mockMvc.perform(put("/change-password")
                            .param("username", username)
                            .param("oldPassword", oldPassword)
                            .param("newPassword", newPassword))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("User not found"));

            verify(authenticationService, times(1)).authenticate(username, oldPassword);
        }
}