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

    @Autowired
    private AuthService authService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @PostMapping("/register")
    public String register(@RequestBody Customer customer) {
        String encodedPassword = bCryptPasswordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
        String token = authService.registerCustomer(customer);
        System.out.println("Registration endpoint called for user: " + customer.getEmail());
        return token;
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) throws Exception {
        String token = authService.authenticateCustomer(authRequest.getEmail(), authRequest.getPassword());
        System.out.println("Login endpoint called for user: " + authRequest.getEmail());
        return token;
    }
}
