//package org.gymCrm.hibernate.util;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.gymCrm.hibernate.config.jwt.JwtUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import java.security.NoSuchAlgorithmException;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class JwtUtilTest {
//
//    private JwtUtil jwtUtil;
//    private SecretKey secretKey;
//
//    @BeforeEach
//    public void setUp() throws NoSuchAlgorithmException {
//        secretKey = KeyGenerator.getInstance("HmacSHA256").generateKey();
//        jwtUtil = new JwtUtil();
//    }
//
////    @Test
////    public void testGenerateToken() {
////        String username = "testuser";
////        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
////
////        String token = jwtUtil.generateToken(username, roles);
////
////        assertNotNull(token);
////        assertFalse(token.isEmpty());
////
////        // Verify the claims
////        Claims claims = Jwts.parser()
////                .setSigningKey(secretKey)
////                .build()
////                .parseClaimsJws(token)
////                .getBody();
////
////        assertEquals(username, claims.getSubject());
////        assertEquals(roles, claims.get("roles"));
////    }
//
//    @Test
//    public void testValidateToken() {
//        String username = "testuser";
//        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
//        String token = jwtUtil.generateToken(username, roles);
//
//        assertTrue(jwtUtil.validateToken(token, username));
//    }
//
//    @Test
//    public void testValidateToken_InvalidUsername() {
//        String username = "testuser";
//        String wrongUsername = "wronguser";
//        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
//        String token = jwtUtil.generateToken(username, roles);
//
//        assertFalse(jwtUtil.validateToken(token, wrongUsername));
//    }
//
//    @Test
//    public void testBlacklistToken() {
//        String username = "testuser";
//        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
//        String token = jwtUtil.generateToken(username, roles);
//
//        jwtUtil.blacklistToken(token);
//
//        assertTrue(jwtUtil.isTokenBlacklisted(token));
//    }
//
//    @Test
//    public void testIsTokenExpired() throws InterruptedException {
//        String username = "testuser";
//        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
//        String token = jwtUtil.generateToken(username, roles);
//
//        Thread.sleep(3600 * 1000 + 1000);
//
//        assertTrue(jwtUtil.isTokenExpired(token));
//    }
//
////    @Test
////    public void testExtractUsername() {
////        String username = "testuser";
////        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
////        String token = jwtUtil.generateToken(username, roles);
////
////        String extractedUsername = jwtUtil.extractUsername(token);
////        assertEquals(username, extractedUsername);
////    }
//
//    @Test
//    public void testExtractRoles() {
//        String username = "testuser";
//        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
//        String token = jwtUtil.generateToken(username, roles);
//
//        List<String> extractedRoles = jwtUtil.extractRoles(token);
//        assertEquals(roles, extractedRoles);
//    }
//}
//


