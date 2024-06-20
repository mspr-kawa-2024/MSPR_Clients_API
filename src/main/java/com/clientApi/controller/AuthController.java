package com.clientApi.controller;

import com.clientApi.model.AuthRequest;
import com.clientApi.model.Customer;
import com.clientApi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthController(AuthService authService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authService = authService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @PostMapping("/register")
    public String register(@RequestBody Customer customer) {
        if (customer.getPassword() == null || customer.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
        customer.setPassword(encodedPassword);
        String token = authService.registerCustomer(customer);
        System.out.println("Registration endpoint called for user: " + customer.getEmail());
        return token;
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) throws Exception {
        System.out.println("Login endpoint called for user: " + authRequest.getEmail());
        if (authRequest.getPassword() == null || authRequest.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        String token = authService.authenticateCustomer(authRequest.getEmail(), authRequest.getPassword());
        System.out.println("Login endpoint called for user: " + authRequest.getEmail());
        return token;
    }
}
