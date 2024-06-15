package com.clientApi.client;

import com.clientApi.Client;
import com.clientApi.ClientController;
import com.clientApi.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClientControllerTest {

    @Mock
    private ClientService clientService;

    private ClientController clientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientController = new ClientController(clientService);
    }

    @Test
    void getClients_ReturnsListOfClients() {
        // Arrange
        List<Client> clients = Arrays.asList(new Client(), new Client());
        when(clientService.getClients()).thenReturn(clients);

        // Act
        List<Client> result = clientController.getClients();

        // Assert
        assertEquals(clients.size(), result.size());
    }

    @Test
    void registerNewClient_ClientIsAdded() {
        // Arrange
        Client client = new Client();

        // Act
        clientController.registerNewClient(client);

        // Assert
        verify(clientService, times(1)).addNewClient(client);
    }

    @Test
    void updateClient_ClientIsUpdated() {
        // Arrange
        Long clientId = 1L;
        String name = "John";
        String email = "john@example.com";

        // Act
        clientController.updateClient(clientId, name, email);

        // Assert
        verify(clientService, times(1)).updateClient(clientId, name, email);
    }

    @Test
    void deleteClient_ClientIsDeleted() {
        // Arrange
        Long clientId = 1L;

        // Act
        clientController.deleteClient(clientId);

        // Assert
        verify(clientService, times(1)).deleteClient(clientId);
    }
}
