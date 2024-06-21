package com.clientApi.config;

import com.clientApi.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtil = new JwtUtil();
    }

    @Test
    public void testExtractUsername() throws Exception {
        String token = generateTestToken("testUser");

        // Use reflection to call the private extractAllClaims method
        Method extractAllClaimsMethod = JwtUtil.class.getDeclaredMethod("extractAllClaims", String.class);
        extractAllClaimsMethod.setAccessible(true);
        Claims claims = (Claims) extractAllClaimsMethod.invoke(jwtUtil, token);

        // Verify the result of extractUsername
        Method extractUsernameMethod = JwtUtil.class.getDeclaredMethod("extractClaim", String.class, Function.class);
        extractUsernameMethod.setAccessible(true);
        String resultUsername = (String) extractUsernameMethod.invoke(jwtUtil, token, (Function<Claims, String>) Claims::getSubject);

        assertEquals("testUser", resultUsername);
    }

    @Test
    public void testGenerateToken() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        assertNotNull(token);

        // Use JwtUtil to extract username from token and verify
        String extractedUsername = jwtUtil.extractUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    public void testValidateToken() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        boolean isValid = jwtUtil.validateToken(token, username);

        assertTrue(isValid);
    }

    private String generateTestToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, "secret")
                .compact();
    }
}
