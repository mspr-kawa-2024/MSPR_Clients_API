package com.clientApi.client;

import com.clientApi.controller.CustomerController;
import com.clientApi.model.Customer;
import com.clientApi.service.CustomerService;
import com.clientApi.config.RabbitMQReceiver;
import com.clientApi.config.RabbitMQSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @Mock
    private RabbitMQSender rabbitMQSender;

    @Mock
    private RabbitMQReceiver rabbitMQReceiver;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void testGetClients() throws Exception {
        Customer customer = new Customer(1L, "John", "Doe", "john.doe@example.com", "password", null, null);
        List<Customer> customers = List.of(customer);
        when(customerService.getClients()).thenReturn(customers);

        mockMvc.perform(get("/api/v1/client"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).getClients();
    }

    @Test
    void testGetClientById() throws Exception {
        Customer customer = new Customer(1L, "John", "Doe", "john.doe@example.com", "password", null, null);
        when(customerService.getClientById(anyLong())).thenReturn(customer);

        mockMvc.perform(get("/api/v1/client/1"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).getClientById(1L);
    }

    @Test
    void testRegisterNewClient() throws Exception {
        Customer customer = new Customer(1L, "John", "Doe", "john.doe@example.com", "password", null, null);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("hashedPassword");

        mockMvc.perform(post("/api/v1/client")
                        .contentType("application/json")
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).addNewClient(any(Customer.class));
    }

    @Test
    void testUpdateClient() throws Exception {
        mockMvc.perform(put("/api/v1/client/1")
                        .param("name", "Jane")
                        .param("lastname", "Smith")
                        .param("email", "jane.doe@example.com"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).updateClient(1L, "Jane", "Smith", "jane.doe@example.com");
    }

    @Test
    void testDeleteClient() throws Exception {
        mockMvc.perform(delete("/api/v1/client/1"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).deleteClient(1L);
    }

    @Test
    void testGetCustomerOrderProducts() throws Exception {
        when(customerService.getClientById(anyLong())).thenReturn(new Customer());
        when(rabbitMQReceiver.getReceivedMessage()).thenReturn("commandOfClient");

        mockMvc.perform(get("/api/v1/client/1/orders/1/products"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).getClientById(1L);
        verify(rabbitMQSender, times(1)).sendClientIdAndOrderId("1,1");
    }
}
