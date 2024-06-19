package com.clientApi.client;

import com.clientApi.config.SecurityConfig;
import com.clientApi.controller.CustomerController;
import com.clientApi.filter.JwtRequestFilter;
import com.clientApi.service.CustomUserDetailsService;
import com.clientApi.service.CustomerService;
import com.clientApi.util.JwtUtil;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
@Ignore
@WebMvcTest(CustomerController.class)
@Import(SecurityConfig.class)*/
public class SecurityConfigIntegrationTest {

    /*
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserDetails userDetails;

    @MockBean
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("testUser")
                .password(new BCryptPasswordEncoder().encode("testPassword"))
                .authorities(new ArrayList<>())
                .build();

        when(customUserDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
    }

    @Test
    public void testPublicEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/public"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testPrivateEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/private"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/v1/private"))
                .andExpect(status().isUnauthorized());
    }*/
}
