package com.clientApi.client;

import com.clientApi.model.Customer;
import com.clientApi.repository.CustomerRepository;
import com.clientApi.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetClients() {
        customerService.getClients();
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testGetClientById() {
        Customer customer = new Customer(1L, "John", "Doe", "john.doe@example.com", "password", null, null);
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.getClientById(1L);

        assertEquals(customer, foundCustomer);
    }

    @Test
    void testAddNewClientThrowsExceptionWhenEmailIsTaken() {
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "password", null, null);
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.of(customer));

        assertThrows(IllegalStateException.class, () -> customerService.addNewClient(customer));

        verify(customerRepository, never()).save(any());
    }

    @Test
    void testAddNewClientSavesWhenEmailIsNotTaken() {
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "password", null, null);
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        customerService.addNewClient(customer);

        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testDeleteClient() {
        when(customerRepository.existsById(anyLong())).thenReturn(true);
        customerService.deleteClient(1L);
        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateClient() {
        Customer customer = new Customer(1L, "John", "Doe", "john.doe@example.com", "password", null, null);
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        customerService.updateClient(1L, "Jane", "Smith", "jane.smith@example.com");

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).findByEmail("jane.smith@example.com");
        verify(customerRepository, times(1)).save(customer);
        assertEquals("Jane", customer.getFirstName());
        assertEquals("Smith", customer.getLastName());
        assertEquals("jane.smith@example.com", customer.getEmail());
    }

    @Test
    void testUpdateClientWithSameEmail() {
        Customer customer = new Customer(1L, "John", "Doe", "john.doe@example.com", "password", null, null);
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        customerService.updateClient(1L, "Jane", "Smith", "john.doe@example.com");

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, never()).findByEmail(anyString());
        verify(customerRepository, times(1)).save(customer);
        assertEquals("Jane", customer.getFirstName());
        assertEquals("Smith", customer.getLastName());
        assertEquals("john.doe@example.com", customer.getEmail());
    }

    @Test
    void testUpdateClientWithTakenEmail() {
        Customer customer = new Customer(1L, "John", "Doe", "john.doe@example.com", "password", null, null);
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(customerRepository.findByEmail("taken.email@example.com")).thenReturn(Optional.of(new Customer()));

        assertThrows(IllegalStateException.class, () -> customerService.updateClient(1L, "Jane", "Smith", "taken.email@example.com"));

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).findByEmail("taken.email@example.com");
        verify(customerRepository, never()).save(any(Customer.class));
    }
}
