package org.gymCrm.hibernate.util;

import org.gymCrm.hibernate.exception.AuthenticationException;
import org.gymCrm.hibernate.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService{

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticate(String username, String password) {
        return userService.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElseThrow(() -> new AuthenticationException("Authentication failed for user: " + username)).isActive();
    }
}
