package com.clientApi.client;

import com.clientApi.model.Customer;
import com.clientApi.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerRepositoryTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerRepositoryTest customerRepositoryTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveCustomer() {
        Customer customer = new Customer(1L,"John", "Doe", "john.doe@example.com", "password", null, null);
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer savedCustomer = customerRepository.save(customer);

        assertNotNull(savedCustomer.getId());
        assertEquals("John", savedCustomer.getFirstName());
        assertEquals("Doe", savedCustomer.getLastName());
        assertEquals("john.doe@example.com", savedCustomer.getEmail());
    }

    @Test
    void testFindCustomerById() {
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "password", null, null);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

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
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));

        List<Customer> customers = customerRepository.findAll();
        assertEquals(2, customers.size());
    }

    @Test
    void testUpdateCustomer() {
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "password", null, null);
        when(customerRepository.save(customer)).thenReturn(customer);

        customer.setFirstName("Jane");
        customer.setEmail("jane.doe@example.com");
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer updatedCustomer = customerRepository.save(customer);

        assertEquals("Jane", updatedCustomer.getFirstName());
        assertEquals("jane.doe@example.com", updatedCustomer.getEmail());
    }

    @Test
    void testDeleteCustomer() {
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "password", null, null);
        when(customerRepository.save(customer)).thenReturn(customer);
        doNothing().when(customerRepository).delete(customer);

        Customer savedCustomer = customerRepository.save(customer);
        customerRepository.delete(savedCustomer);

        when(customerRepository.findById(savedCustomer.getId())).thenReturn(Optional.empty());
        Optional<Customer> deletedCustomer = customerRepository.findById(savedCustomer.getId());

        assertFalse(deletedCustomer.isPresent());
    }
}