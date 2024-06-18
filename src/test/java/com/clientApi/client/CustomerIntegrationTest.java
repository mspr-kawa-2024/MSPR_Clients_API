package com.clientApi.client;

import com.clientApi.model.Customer;
import com.clientApi.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CustomerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void testGetClients() {
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "password", null, null);
        customerRepository.save(customer);

        ResponseEntity<Customer[]> response = restTemplate.getForEntity("/api/v1/client", Customer[].class);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, Objects.requireNonNull(response.getBody()).length);
    }

    @Test
    void testAddNewClient() {
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "password", null, null);
        ResponseEntity<Void> response = restTemplate.postForEntity("/api/v1/client", customer, Void.class);
        assertEquals(200, response.getStatusCode().value());

        Customer found = customerRepository.findByEmail("john.doe@example.com").orElse(null);
        assert found != null;
        assertEquals("John", found.getFirstName());
    }

    @Test
    void testUpdateClient() {
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "password", null, null);
        customerRepository.save(customer);

        HttpEntity<?> requestEntity = new HttpEntity<>(null);
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

        restTemplate.delete("/api/v1/client/" + customer.getId());

        boolean exists = customerRepository.existsById(customer.getId());
        assertFalse(exists);
    }
}
