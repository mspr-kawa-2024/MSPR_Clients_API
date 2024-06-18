package com.clientApi.client;

import com.clientApi.model.Customer;
import com.clientApi.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(null, "John", "Doe", "john.doe@example.com", "password", LocalDate.now(), LocalDate.now());
        customerRepository.save(customer);
    }

    @Test
    void testFindByEmail() {
        Optional<Customer> foundCustomer = customerRepository.findByEmail("john.doe@example.com");
        assertTrue(foundCustomer.isPresent());
        assertEquals("john.doe@example.com", foundCustomer.get().getEmail());
    }

    @Test
    void testFindByEmailAndPassword() {
        Optional<Customer> foundCustomer = customerRepository.findByEmailAndPassword("john.doe@example.com", "password");
        assertTrue(foundCustomer.isPresent());
        assertEquals("john.doe@example.com", foundCustomer.get().getEmail());
    }

    @Test
    void testSaveCustomer() {
        Customer newCustomer = new Customer(null, "Jane", "Smith", "jane.smith@example.com", "password123", LocalDate.now(), LocalDate.now());
        Customer savedCustomer = customerRepository.save(newCustomer);
        assertNotNull(savedCustomer.getId());
    }

    @Test
    void testUpdateCustomer() {
        Customer updatedCustomer = customerRepository.findById(customer.getId()).orElseThrow();
        updatedCustomer.setFirstName("Johnny");
        customerRepository.save(updatedCustomer);

        Customer foundCustomer = customerRepository.findById(customer.getId()).orElseThrow();
        assertEquals("Johnny", foundCustomer.getFirstName());
    }

    @Test
    void testDeleteCustomer() {
        customerRepository.deleteById(customer.getId());
        Optional<Customer> deletedCustomer = customerRepository.findById(customer.getId());
        assertFalse(deletedCustomer.isPresent());
    }

    @Test
    void testFindAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        assertFalse(customers.isEmpty());
        assertEquals(1, customers.size());
    }
}
