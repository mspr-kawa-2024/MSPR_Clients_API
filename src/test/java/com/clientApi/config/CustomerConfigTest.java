package com.clientApi.config;

import com.clientApi.model.Customer;
import com.clientApi.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class CustomerConfigTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerConfig customerConfig;

    @Captor
    private ArgumentCaptor<List<Customer>> customerCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCommandLineRunner() throws Exception {
        customerConfig.commandLineRunner(repository).run();

        verify(repository).saveAll(customerCaptor.capture());
        List<Customer> customers = customerCaptor.getValue();
        assertEquals(4, customers.size());

        Customer customer1 = customers.get(0);
        assertEquals("firstName1", customer1.getFirstName());
        assertEquals("lastName1", customer1.getLastName());
        assertEquals("email1@gmail.com", customer1.getEmail());
        assertTrue(customerConfig.encoder.matches("password1", customer1.getPassword()));

        Customer customer2 = customers.get(1);
        assertEquals("firstName2", customer2.getFirstName());
        assertEquals("lastName2", customer2.getLastName());
        assertEquals("email2@gmail.com", customer2.getEmail());
        assertTrue(customerConfig.encoder.matches("password2", customer2.getPassword()));

        Customer customer3 = customers.get(2);
        assertEquals("firstName3", customer3.getFirstName());
        assertEquals("lastName3", customer3.getLastName());
        assertEquals("email3@gmail.com", customer3.getEmail());
        assertTrue(customerConfig.encoder.matches("password3", customer3.getPassword()));

        Customer customer4 = customers.get(3);
        assertEquals("firstName4", customer4.getFirstName());
        assertEquals("lastName4", customer4.getLastName());
        assertEquals("email4@gmail.com", customer4.getEmail());
        assertTrue(customerConfig.encoder.matches("password4", customer4.getPassword()));
    }

    @Test
    void testRestTemplate() {
        RestTemplate restTemplate = customerConfig.restTemplate(new RestTemplateBuilder());
        assertNotNull(restTemplate);
    }

    @Configuration
    static class TestConfig {

        @Bean
        public BCryptPasswordEncoder encoder() {
            return new BCryptPasswordEncoder();
        }
    }
}
