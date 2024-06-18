package com.clientApi.client;

import com.clientApi.model.AuthRequest;
import com.clientApi.model.Customer;
import com.clientApi.repository.CustomerRepository;
import com.clientApi.service.CustomUserDetailsService;
import com.clientApi.util.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private UserDetails userDetails;



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
    public void testRegisterAndAuthenticate() throws Exception {
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setPassword(new BCryptPasswordEncoder().encode("password"));

        // Register user
        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User registered successfully")));

        // Authenticate user
        AuthRequest authReq = new AuthRequest();
        authReq.setEmail("test@example.com");
        authReq.setPassword("password");

        MvcResult result = mockMvc.perform(post("/api/v1/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String token = response.substring(response.indexOf(":\"") + 2, response.length() - 2);

        // Verify the token
        String username = jwtUtil.extractUsername(token);
        Assertions.assertEquals("test@example.com", username);
    }
}

