package com.clientApi.client;

import com.clientApi.model.Customer;
import com.clientApi.repository.CustomerRepository;
import com.clientApi.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@ActiveProfiles("test")
public class CustomerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JwtUtil jwtTokenUtil;


    private String token;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
        token = jwtTokenUtil.generateToken("testuser"); // Utilisez un nom d'utilisateur de test
    }

    @Test
    void testGetClients() {
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "password", null, null);
        customerRepository.save(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Customer[]> response = restTemplate.exchange("/api/v1/client", HttpMethod.GET, requestEntity, Customer[].class);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, Objects.requireNonNull(response.getBody()).length);
    }

    @Test
    void testUpdateClient() {
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "password", null, null);
        customerRepository.save(customer);

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
