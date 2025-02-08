package org.gymCrm.hibernate.config.jwt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.gymCrm.hibernate.dto.AuthResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            Map<String, String> credentials = new ObjectMapper().readValue(request.getInputStream(),
                    new TypeReference<>() {
                    });
            String username = credentials.get("username");
            String password = credentials.get("password");

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read authentication request", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = jwtUtil.generateToken(userDetails.getUsername(), roles); // Include roles in the token

        AuthResponse authResponse = new AuthResponse(token);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getWriter(), authResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        logger.debug("Authentication failed: " + failed.getMessage());

        int status = HttpServletResponse.SC_UNAUTHORIZED;

        if (failed instanceof LockedException) {
            status = HttpServletResponse.SC_FORBIDDEN;
            logger.debug("User is blocked - returning status: " + status);
        }

        response.setStatus(status);
        response.setContentType("application/json");

        Map<String, String> errorResponse = Map.of("error",
                status == HttpServletResponse.SC_UNAUTHORIZED ? "Unauthorized" : "Forbidden", "message",
                failed.getMessage());

        new ObjectMapper().writeValue(response.getWriter(), errorResponse);
    }
}