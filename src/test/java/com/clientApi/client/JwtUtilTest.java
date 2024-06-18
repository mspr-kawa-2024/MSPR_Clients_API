package com.clientApi.client;

import com.clientApi.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateToken() {
        UserDetails userDetails = new User("testUser", "password", new ArrayList<>());
        String token = jwtUtil.generateToken(userDetails);

        assertNotNull(token);
        assertEquals("testUser", jwtUtil.extractUsername(token));
    }

    @Test
    public void testValidateToken() {
        UserDetails userDetails = new User("testUser", "password", new ArrayList<>());
        String token = jwtUtil.generateToken(userDetails);

        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    public void testExtractUsername() {
        UserDetails userDetails = new User("testUser", "password", new ArrayList<>());
        String token = jwtUtil.generateToken(userDetails);

        assertEquals("testUser", jwtUtil.extractUsername(token));
    }

    @Test
    public void testExtractExpiration() {
        UserDetails userDetails = new User("testUser", "password", new ArrayList<>());
        String token = jwtUtil.generateToken(userDetails);

        Date expiration = jwtUtil.extractExpiration(token);
        assertNotNull(expiration);
    }

    @Test
    public void testExtractClaim() {
        UserDetails userDetails = new User("testUser", "password", new ArrayList<>());
        String token = jwtUtil.generateToken(userDetails);

        Claims claims = jwtUtil.extractAllClaims(token);
        assertEquals("testUser", claims.getSubject());
    }
}
