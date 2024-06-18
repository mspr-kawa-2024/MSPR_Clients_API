package com.clientApi.client;



import com.clientApi.model.AuthRequest;
import com.clientApi.model.AuthResponse;
import com.clientApi.model.Customer;
import com.clientApi.controller.AuthController;
import com.clientApi.repository.CustomerRepository;
import com.clientApi.service.CustomUserDetailsService;
import com.clientApi.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtUtil jwtTokenUtil;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

/*    @Test
    public void testCreateAuthenticationToken() throws Exception {
        AuthRequest authenticationRequest = new AuthRequest();
        authenticationRequest.setEmail("test@example.com");
        authenticationRequest.setPassword("password");

        UserDetails userDetails = mock(UserDetails.class);

        when(userDetailsService.loadUserByUsername(authenticationRequest.getEmail())).thenReturn(userDetails);
        when(jwtTokenUtil.generateToken(String.valueOf(userDetails))).thenReturn("testToken");

        ResponseEntity<?> response = authController.createAuthenticationToken(authenticationRequest);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("testToken", ((AuthResponse) Objects.requireNonNull(response.getBody())).getJwt());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername(authenticationRequest.getEmail());
        verify(jwtTokenUtil).generateToken(String.valueOf(userDetails));
    }

    @Test
    public void testSaveUser() throws Exception {
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setPassword("password");

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        ResponseEntity<?> response = authController.saveUser(customer);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("User registered successfully", response.getBody());

        verify(customerRepository).save(any(Customer.class));
    }*/
}
