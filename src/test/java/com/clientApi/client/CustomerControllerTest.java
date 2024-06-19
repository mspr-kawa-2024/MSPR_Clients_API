package com.clientApi.client;

import com.clientApi.controller.CustomerController;
import com.clientApi.model.Customer;
import com.clientApi.service.CustomerService;
import com.clientApi.config.RabbitMQReceiver;
import com.clientApi.config.RabbitMQSender;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CustomerControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    @Mock
    private RabbitMQSender rabbitMQSender;

    @Mock
    private RabbitMQReceiver rabbitMQReceiver;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void testGetClients() throws Exception {
        List<Customer> customers = Arrays.asList(new Customer(), new Customer());
        when(customerService.getClients()).thenReturn(customers);

        mockMvc.perform(get("/api/v1/client"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(customerService, times(1)).getClients();
    }

    @Test
    public void testGetClientById() throws Exception {
        Customer customer = new Customer();
        when(customerService.getClientById(1L)).thenReturn(customer);

        mockMvc.perform(get("/api/v1/client/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(customerService, times(1)).getClientById(1L);
    }

    @Test
    public void testUpdateClient() throws Exception {
        doNothing().when(customerService).updateClient(1L, "John", "Doe", "john@example.com");

        mockMvc.perform(put("/api/v1/client/1")
                        .param("name", "John")
                        .param("lastname", "Doe")
                        .param("email", "john@example.com"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).updateClient(1L, "John", "Doe", "john@example.com");
    }

    @Test
    public void testDeleteClient() throws Exception {
        doNothing().when(customerService).deleteClient(1L);

        mockMvc.perform(delete("/api/v1/client/1"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).deleteClient(1L);
    }

    /*
    @Test
    public void testGetCustomerOrderProducts() throws Exception {
        when(customerService.getClientById(1L)).thenReturn(new Customer());
        doNothing().when(rabbitMQSender).sendClientIdAndOrderId(anyString());
        when(rabbitMQReceiver.getReceivedMessage()).thenReturn("Products");

        mockMvc.perform(get("/api/v1/client/1/orders/1/products"))
                .andExpect(status().isOk())
                .andExpect(content().string("Products"));

        verify(rabbitMQSender, times(1)).sendClientIdAndOrderId(anyString());
        verify(rabbitMQReceiver, times(1)).getReceivedMessage();
    }*/

}
