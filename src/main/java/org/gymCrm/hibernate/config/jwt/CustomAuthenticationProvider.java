package org.gymCrm.hibernate.config.jwt;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.exception.AuthenticationException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final BruteForceProtectionService bruteForceProtectionService;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder,
                                        BruteForceProtectionService bruteForceProtectionService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.bruteForceProtectionService = bruteForceProtectionService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (bruteForceProtectionService.isBlocked(username)) {
            throw new LockedException("User is blocked for 5 minutes due to multiple failed login attempts.");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        log.debug("Loaded user: {}, Stored password: {}", userDetails.getUsername(), userDetails.getPassword());

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            log.debug("Password match successful for user: {}", username);
            bruteForceProtectionService.loginSucceeded(username);
            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        } else {
            log.warn("Password mismatch for user: {}", username);
            bruteForceProtectionService.loginFailed(username);
            throw new BadCredentialsException("Invalid username or password.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}