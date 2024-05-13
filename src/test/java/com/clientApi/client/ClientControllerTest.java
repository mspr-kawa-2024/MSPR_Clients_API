package com.clientApi.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    @Test
    void authenticateClient_ValidCredentials_ReturnsOkResponse() {
        // Arrange
        ClientCredentials credentials = new ClientCredentials("john@example.com", "password");
        Map<String, String> response = Map.of("message", "Authentication successful");
        when(clientService.authenticateClient(credentials.getEmail(), credentials.getPassword())).thenReturn(response);

        // Act
        ResponseEntity<Map<String, String>> result = clientController.authenticateClient(credentials);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    /**
    @Test
    void authenticateClient_InvalidCredentials_ReturnsUnauthorizedResponse() {
        // Arrange
        ClientCredentials credentials = new ClientCredentials("john@example.com", "wrongPassword");
        Map<String, String> response = Map.of("message", "Invalid credentials");
        when(clientService.authenticateClient(credentials.getEmail(), credentials.getPassword())).thenReturn(response);

        // Act
        ResponseEntity<Map<String, String>> result = clientController.authenticateClient(credentials);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals(response, result.getBody());
    }*/
}
