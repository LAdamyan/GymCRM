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

    public BackendJwtUtil(@Value("${backend.secret.key}") String backendSecretKey) {
        if (backendSecretKey == null || backendSecretKey.isEmpty()) {
            log.error("[BackendJwtUtil] Backend secret key is not configured properly!");
            throw new RuntimeException("Backend secret key is not configured");
        }

        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(backendSecretKey));
        log.info("[BackendJwtUtil] Successfully loaded backend secret key.");

    }
    public String generateBackendToken() {
        return Jwts.builder()
                .setSubject("main-microservice")
                .setIssuer("main-microservice")       // Required
                .setAudience("trainer-workload-service") // Required
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                .signWith(secretKey, SignatureAlgorithm.HS256) // Use backend.secret.key
                .compact();

    }

    public boolean validateBackendToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            log.info("[BackendJwtUtil] Backend JWT Token is valid: {}", token);
            return true;
        } catch (Exception e) {
            log.error("[BackendJwtUtil] Error during token validation: {}", e.getMessage());
            return false;
        }
    }
}