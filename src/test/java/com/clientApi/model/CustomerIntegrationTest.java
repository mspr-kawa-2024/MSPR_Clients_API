package com.clientApi.model;

import com.clientApi.CustomerApplication;
import com.clientApi.model.Customer;
import com.clientApi.repository.CustomerRepository;
import com.clientApi.util.JwtUtil;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


//@DataJpaTest
@SpringBootTest(classes = CustomerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@ActiveProfiles("test")
public class CustomerIntegrationTest {


    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private JwtUtil jwtTokenUtil;


    private String token;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
        // Ajoutez un utilisateur de test
        customerRepository.deleteAll();
        Customer user = new Customer(1L, "testuser", "password", "testuser@example.com", new BCryptPasswordEncoder().encode("passwordtest"), null, null);
        customerRepository.save(user);
        Mockito.when(customerRepository.findByEmail("testuser@example.com")).thenReturn(Optional.of(user));
        Mockito.when(customerRepository.save(Mockito.any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        token = jwtTokenUtil.generateToken("testuser@example.com"); // Utilisez un nom d'utilisateur de test
    }

    @Test
    void testGetClients() {
        Customer customer = new Customer(2L,"John", "Doe", "john.doe@example.com", "password", null, null);
        customerRepository.save(customer);
        Mockito.when(customerRepository.findAll()).thenReturn(List.of(customer));
        Mockito.when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Customer[]> response = restTemplate.exchange("/api/v1/client", HttpMethod.GET, requestEntity, Customer[].class);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, Objects.requireNonNull(response.getBody()).length);
    }

    @Test
    void testUpdateClient() {
        Customer customer = new Customer(2L,"John", "Doe", "john.doe@example.com", "password", null, null);
        customerRepository.save(customer);
        Mockito.when(customerRepository.findById(2L)).thenReturn(Optional.of(customer));
        Mockito.when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<?> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<Void> response = restTemplate.exchange("/api/v1/client/" + customer.getId() + "?name=Jane&email=jane.doe@example.com", HttpMethod.PUT, requestEntity, Void.class);
        assertEquals(200, response.getStatusCode().value());

        Customer updatedCustomer = customerRepository.findById(customer.getId()).orElse(null);
        assert updatedCustomer != null;
        assertEquals("Jane", updatedCustomer.getFirstName());
        assertEquals("jane.doe@example.com", updatedCustomer.getEmail());
    }

    @Test
    void testDeleteClient() {
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "password", null, null);
        customerRepository.save(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        restTemplate.exchange("/api/v1/client/" + customer.getId(), HttpMethod.DELETE, requestEntity, Void.class);

        boolean exists = customerRepository.existsById(customer.getId());
        assertFalse(exists);
    }
}
