package com.clientApi.client;

import com.clientApi.model.Customer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerTest {



    @Test
    void testToString() {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPassword("password");
        LocalDate currentDate = LocalDate.now();
        customer.setCreationDate(currentDate);
        customer.setUpdateDate(currentDate);

        // Création de l'objet Client avec les valeurs attendues
        Customer expectedCustomer = new Customer(1L, "John", "Doe", "john.doe@example.com", "password", currentDate, currentDate);

        // Assert
        assertEquals(expectedCustomer.toString(), customer.toString());
    }
}
