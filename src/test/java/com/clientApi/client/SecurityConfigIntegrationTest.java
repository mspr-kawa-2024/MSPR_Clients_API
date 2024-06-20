package com.clientApi.client;

import com.clientApi.CustomerApplication;
import com.clientApi.config.SecurityConfig;
import com.clientApi.config.WebConfig;
import com.clientApi.filter.JwtRequestFilter;
import com.clientApi.service.CustomUserDetailsService;
import com.clientApi.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {CustomerApplication.class, SecurityConfig.class, CustomerService.class,  CustomUserDetailsService.class, JwtRequestFilter.class, WebConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SecurityConfigIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPublicEndpoints() throws Exception {
        mockMvc.perform(get("/auth/login"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/auth/register"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testSecuredEndpoints() throws Exception {
        mockMvc.perform(get("/api/v1/client"))
                .andExpect(status().isOk());
    }

   /* @Test
    void testSecuredEndpointsWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/client"))
                .andExpect(status().isUnauthorized());
    }*/
}
