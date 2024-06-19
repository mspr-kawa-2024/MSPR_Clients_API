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



    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;



    public String registerCustomer(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
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
            if (passwordEncoder.matches(password, customer.getPassword())) {
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
