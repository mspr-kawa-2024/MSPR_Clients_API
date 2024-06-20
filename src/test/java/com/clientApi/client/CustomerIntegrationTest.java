package com.clientApi.client;

import com.clientApi.model.Customer;
import com.clientApi.repository.CustomerRepository;
import com.clientApi.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
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
        Customer testUser = new Customer("testuser", "User", "testuser@example.com", "password", null, null);
        customerRepository.save(testUser); // Sauvegarder un utilisateur de test
        token = jwtTokenUtil.generateToken("testuser@example.com"); // Utiliser l'email comme sujet du token
    }

    @Test
    void testGetClients() {
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "password", null, null);
        customerRepository.save(customer); // Sauvegarder un nouveau client

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Définir le header Authorization
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Customer[]> response = restTemplate.exchange("/api/v1/client", HttpMethod.GET, requestEntity, Customer[].class);
        assertEquals(200, response.getStatusCode().value()); // Vérifier que le statut est 200
        assertEquals(2, Objects.requireNonNull(response.getBody()).length); // Vérifier qu'il y a un client dans la réponse
    }

    @Test
    void testUpdateClient() {
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "password", null, null);
        customerRepository.save(customer); // Sauvegarder un nouveau client

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Définir le header Authorization
        headers.set("Content-Type", "application/json"); // Définir le type de contenu

        String updateJson = "{\"firstName\":\"Jane\", \"lastName\":\"Doe\", \"email\":\"jane.doe@example.com\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(updateJson, headers);

        ResponseEntity<Map> response = restTemplate.exchange("/api/v1/client/" + customer.getId(), HttpMethod.PUT, requestEntity, Map.class); // Utiliser PUT au lieu de POST
        assertEquals(200, response.getStatusCode().value()); // Vérifier que le statut est 200

        Customer updatedCustomer = customerRepository.findById(customer.getId()).orElse(null);
        assert updatedCustomer != null;
        assertEquals("Jane", updatedCustomer.getFirstName()); // Vérifier que le prénom a été mis à jour
        assertEquals("jane.doe@example.com", updatedCustomer.getEmail()); // Vérifier que l'email a été mis à jour
    }

    @Test
    void testDeleteClient() {
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "password", null, null);
        customerRepository.save(customer); // Sauvegarder un nouveau client

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Définir le header Authorization
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        restTemplate.exchange("/api/v1/client/" + customer.getId(), HttpMethod.DELETE, requestEntity, Void.class); // Supprimer le client

        boolean exists = customerRepository.existsById(customer.getId());
        assertFalse(exists); // Vérifier que le client n'existe plus
    }

}
