package com.clientApi.service;

import com.clientApi.model.Customer;
import com.clientApi.repository.CustomerRepository;
import com.clientApi.service.AuthService;
import com.clientApi.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterCustomer() {
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setPassword("password");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtUtil.generateToken(anyString())).thenReturn("token");

        String result = authService.registerCustomer(customer);

        assertEquals("token", result);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testAuthenticateCustomer_Success() throws Exception {
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setPassword("password");

        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches("password", "password")).thenReturn(true);
        when(jwtUtil.generateToken("test@example.com")).thenReturn("token");

        String result = authService.authenticateCustomer("test@example.com", "password");

        assertEquals("token", result);
    }

    @Test
    public void testAuthenticateCustomer_InvalidCredentials() {
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setPassword("password");

        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches("wrongPassword", "password")).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> {
            authService.authenticateCustomer("test@example.com", "wrongPassword");
        });

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    public void testAuthenticateCustomer_UserNotFound() {
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            authService.authenticateCustomer("test@example.com", "password");
        });

        assertEquals("User not found", exception.getMessage());
    }
}
