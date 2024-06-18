package com.clientApi.client;

import com.clientApi.filter.JwtRequestFilter;
import com.clientApi.service.CustomUserDetailsService;
import com.clientApi.util.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class JwtRequestFilterTest {

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    private UserDetails userDetails;
    private String jwt;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userDetails = User.builder()
                .username("testUser")
                .password("testPassword")
                .authorities(new ArrayList<>())
                .build();

        jwt = "Bearer " + jwtUtil.generateToken(userDetails);

        SecurityContextHolder.clearContext();
    }



    @Test
    public void testDoFilterInternalWithValidToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", jwt);

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        when(jwtUtil.extractUsername(jwt.substring(7))).thenReturn(userDetails.getUsername());
        when(customUserDetailsService.loadUserByUsername(userDetails.getUsername())).thenReturn(userDetails);
        when(jwtUtil.validateToken(jwt.substring(7), userDetails)).thenReturn(true);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertEquals(userDetails.getUsername(), authentication.getName());
    }

    @Test
    public void testDoFilterInternalWithoutToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        Assertions.assertEquals(null, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testDoFilterInternalWithInvalidToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalidToken");

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        when(jwtUtil.extractUsername("invalidToken")).thenReturn(null);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        Assertions.assertEquals(null, SecurityContextHolder.getContext().getAuthentication());
    }
}
