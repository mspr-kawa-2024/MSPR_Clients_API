package com.clientApi.client;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientTest {



    @Test
    void testToString() {

        Client client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setEmail("john.doe@example.com");
        client.setPassword("password");
        LocalDate currentDate = LocalDate.now();
        client.setCreationDate(currentDate);
        client.setUpdateDate(currentDate);

        // Création de l'objet Client avec les valeurs attendues
        Client expectedClient = new Client(1L, "John", "Doe", "john.doe@example.com", "password", currentDate, currentDate);

        // Assert
        assertEquals(expectedClient.toString(), client.toString());
    }
}
