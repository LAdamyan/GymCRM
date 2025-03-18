package org.gymCrm.hibernate.config.jwt.backendJwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
@Slf4j
@Component
public class BackendJwtUtil {

    private final SecretKey secretKey;

    @Value("${backend.secret.key}")
    private String backendSecretKey;

    public BackendJwtUtil(@Value("${backend.secret.key}") String backendSecretKey) {
        if (backendSecretKey == null || backendSecretKey.isEmpty()) {
            log.error("[BackendJwtUtil] Backend secret key is not configured properly!");
            throw new RuntimeException("Backend secret key is not configured");
        }

        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(backendSecretKey));
        log.info("[BackendJwtUtil] Successfully loaded backend secret key.");

    }
    public String generateBackendToken() {
        String token = Jwts.builder()
                .setSubject("main-microservice") // Subject to identify the service
                .setIssuedAt(new Date()) // Token issuance time
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Token valid for 1 hour
                .signWith(secretKey, SignatureAlgorithm.HS256) // Sign with the secret key
                .compact();

        log.info("[BackendJwtUtil] Generated Backend JWT Token: {}", token); // Log generated token
        return token;
    }

    public boolean validateBackendToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey) // Validate using the secret key
                    .build()
                    .parseClaimsJws(token); // Parse token claims
            log.info("[BackendJwtUtil] Backend JWT Token is valid: {}", token); // Log token validation success
            return true;
        } catch (Exception e) {
            log.error("[BackendJwtUtil] Error during token validation: {}", e.getMessage()); // Log validation failure
            return false;
        }
    }
}