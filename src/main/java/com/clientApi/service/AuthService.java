package com.clientApi.service;


import com.clientApi.model.Customer;
import com.clientApi.repository.CustomerRepository;
import com.clientApi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {



    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(CustomerRepository customerRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtUtil jwtUtil) {
        this.customerRepository = customerRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtil = jwtUtil;
    }


    public String registerCustomer(Customer customer) {
        if (customer.getPassword() == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        System.out.println("Registering customer: " + customer.getEmail());
        System.out.println("Password before encoding: " + customer.getPassword());

        String encodedPassword = bCryptPasswordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
        customerRepository.save(customer);
        String token = jwtUtil.generateToken(customer.getEmail());
        System.out.println("Registration successful for user: " + customer.getEmail());
        System.out.println("Generated Token: " + token);
        return token;
    }

    public String authenticateCustomer(String email, String password) throws Exception {
        Optional<Customer> customerOpt = customerRepository.findByEmail(email);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            System.out.println("Customer found: " + customer.getEmail());
            System.out.println("Password before matching: " + password);
            System.out.println("Encoded password in DB: " + customer.getPassword());
            if (bCryptPasswordEncoder.matches(password, customer.getPassword())) {
                String token = jwtUtil.generateToken(email);
                System.out.println("Login successful for user: " + email);
                System.out.println("Generated Token: " + token);
                return token;
            } else {
                System.out.println("Invalid credentials for user: " + email);
                throw new Exception("Invalid credentials");
            }
        } else {
            System.out.println("User not found: " + email);
            throw new Exception("User not found");
        }
    }
}
