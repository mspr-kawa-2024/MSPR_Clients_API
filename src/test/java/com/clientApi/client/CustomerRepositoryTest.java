package com.clientApi.client;

import com.clientApi.CustomerApplication;
import com.clientApi.model.Customer;
import com.clientApi.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CustomerApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void testSaveCustomer() {
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "password", null, null);
        Customer savedCustomer = customerRepository.save(customer);

        assertNotNull(savedCustomer.getId());
        assertEquals("John", savedCustomer.getFirstName());
        assertEquals("Doe", savedCustomer.getLastName());
        assertEquals("john.doe@example.com", savedCustomer.getEmail());
    }

    @Test
    void testFindCustomerById() {
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "password", null, null);
        Customer savedCustomer = customerRepository.save(customer);

        Optional<Customer> foundCustomer = customerRepository.findById(savedCustomer.getId());
        assertTrue(foundCustomer.isPresent());
        assertEquals("John", foundCustomer.get().getFirstName());
        assertEquals("Doe", foundCustomer.get().getLastName());
        assertEquals("john.doe@example.com", foundCustomer.get().getEmail());
    }

    @Test
    void testFindAllCustomers() {
        Customer customer1 = new Customer("John", "Doe", "john.doe@example.com", "password", null, null);
        Customer customer2 = new Customer("Jane", "Doe", "jane.doe@example.com", "password", null, null);
        customerRepository.save(customer1);
        customerRepository.save(customer2);

        List<Customer> customers = customerRepository.findAll();
        assertEquals(2, customers.size());
    }

    @Test
    void testUpdateCustomer() {
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "password", null, null);
        Customer savedCustomer = customerRepository.save(customer);

        savedCustomer.setFirstName("Jane");
        savedCustomer.setEmail("jane.doe@example.com");
        Customer updatedCustomer = customerRepository.save(savedCustomer);

        assertEquals("Jane", updatedCustomer.getFirstName());
        assertEquals("jane.doe@example.com", updatedCustomer.getEmail());
    }

    @Test
    void testDeleteCustomer() {
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "password", null, null);
        Customer savedCustomer = customerRepository.save(customer);

        customerRepository.delete(savedCustomer);

        Optional<Customer> deletedCustomer = customerRepository.findById(savedCustomer.getId());
        assertFalse(deletedCustomer.isPresent());
    }
}
