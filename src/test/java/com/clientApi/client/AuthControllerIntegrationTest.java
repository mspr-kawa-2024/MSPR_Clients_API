package com.clientApi.client;

import com.clientApi.CustomerApplication;
import com.clientApi.config.SecurityConfig;
import com.clientApi.config.WebConfig;
import com.clientApi.controller.AuthController;
import com.clientApi.model.AuthRequest;
import com.clientApi.model.Customer;
import com.clientApi.repository.CustomerRepository;
import com.clientApi.service.AuthService;
import com.clientApi.service.CustomUserDetailsService;
import com.clientApi.util.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest(classes = {CustomerApplication.class, SecurityConfig.class, WebConfig.class})
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @MockBean
    private JwtUtil jwtUtil;


    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private UserDetails userDetails;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        customerRepository = mock(CustomerRepository.class);
        jwtUtil = mock(JwtUtil.class);
        BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
        customUserDetailsService = mock(CustomUserDetailsService.class);

        String rawPassword = "password";
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);

        when(bCryptPasswordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(bCryptPasswordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwtUtil.generateToken("john@example.com")).thenReturn("token");

        Customer customer = new Customer();
        customer.setEmail("john@example.com");
        customer.setPassword(encodedPassword);

        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(customer));

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("testUser")
                .password(encodedPassword)
                .authorities(new ArrayList<>())
                .build();

        when(customUserDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);

        AuthService authService = new AuthService(customerRepository, bCryptPasswordEncoder, jwtUtil);
        AuthController authController = new AuthController(authService, bCryptPasswordEncoder);

        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testRegisterAndAuthenticate() throws Exception {
        Customer customer = new Customer();
        customer.setEmail("john@example.com");
        customer.setPassword("password");

        // Register user
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("token"));  // Adjust the expected content if needed

        // Authenticate user
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("token"));  // Adjust the expected content if needed
    }
}

