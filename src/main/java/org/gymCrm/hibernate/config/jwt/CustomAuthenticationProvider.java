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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

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
        String clientIP = getClientsIP();

        if (bruteForceProtectionService.isBlocked(username) || bruteForceProtectionService.isBlocked(clientIP)) {
            throw new LockedException("User/IP is blocked for 5 minutes due to multiple failed login attempts.");
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

    private String getClientsIP() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attrs == null){
            return "UNKNOWN";
        }
        HttpServletRequest request = (HttpServletRequest) attrs.getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if(ip == null || ip.isEmpty()){
            ip = request.getRemoteAddr();
        }
        return  ip;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}