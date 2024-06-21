package com.clientApi.config;

import com.clientApi.filter.JwtRequestFilter;
import com.clientApi.service.CustomUserDetailsService;
import com.clientApi.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class JwtRequestFilterTest {

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDoFilterInternal() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtUtil.extractUsername("token")).thenReturn("user");
        UserDetails userDetails = new User("user", "password", Collections.emptyList());
        when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);
        when(jwtUtil.validateToken("token", "user")).thenReturn(true);

        // Using reflection to access the protected method
        Method doFilterInternal = JwtRequestFilter.class.getDeclaredMethod("doFilterInternal", HttpServletRequest.class, HttpServletResponse.class, FilterChain.class);
        doFilterInternal.setAccessible(true);
        doFilterInternal.invoke(jwtRequestFilter, (Object) request, (Object) response, (Object) chain);

        verify(userDetailsService, times(1)).loadUserByUsername("user");
        verify(jwtUtil, times(1)).validateToken("token", "user");
        verify(chain, times(1)).doFilter(request, response);
    }
}
