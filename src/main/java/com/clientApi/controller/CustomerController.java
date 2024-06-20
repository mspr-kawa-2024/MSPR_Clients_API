package com.clientApi.controller;

import com.clientApi.model.Customer;
import com.clientApi.service.CustomerService;
import com.clientApi.config.RabbitMQReceiver;
import com.clientApi.config.RabbitMQSender;
import com.clientApi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/client")
@CrossOrigin(origins = "http://localhost:3000",
        methods = {RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE})
public class CustomerController {

    private final CustomerService customerService;
    private final RabbitMQSender rabbitMQSender;
    private final RabbitMQReceiver rabbitMQReceiver;
    private final JwtUtil jwtUtil;

    @Autowired
    public CustomerController(CustomerService customerService, RabbitMQSender rabbitMQSender, RabbitMQReceiver rabbitMQReceiver, JwtUtil jwtUtil) {
        this.customerService = customerService;
        this.rabbitMQSender = rabbitMQSender;
        this.rabbitMQReceiver = rabbitMQReceiver;
        this.jwtUtil = jwtUtil;
    }


    @GetMapping
    public List<Customer> getClients() {

        return customerService.getClients();
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<Customer> getClientById(@PathVariable Long clientId) {
        Customer customer = customerService.getClientById(clientId);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping(path = "{clientId}")
    public ResponseEntity<Map<String, String>> updateClient(@PathVariable("clientId") Long clientId,
                                                            @RequestBody Customer customerDetails) {
        try {
            Customer updatedCustomer = customerService.updateClient(clientId, customerDetails.getFirstName(), customerDetails.getLastName(), customerDetails.getEmail());
            Customer updtatedCustomer = customerService.getClientById(clientId);
            System.out.println(updtatedCustomer);
            Map<String, String> response = new HashMap<>();

            // Always generate a new token after update
            String newToken = jwtUtil.generateToken(updatedCustomer.getEmail());
            response.put("token", newToken);

            response.put("message", "User updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getMessage()));
        }
    }


/*
    @PutMapping(path = "{clientId}")
    public ResponseEntity<Void> updateClient(@PathVariable("clientId") Long clientId,
                                             @RequestBody Customer customerDetails) {
        customerService.updateClient(clientId, customerDetails.getFirstName(), customerDetails.getLastName(), customerDetails.getEmail());

        return ResponseEntity.ok().build();
    }
*/




    @DeleteMapping(path = "{clientId}")
    public void deleteClient(@PathVariable("clientId") Long clientId) {
        customerService.deleteClient(clientId);
    }

    @GetMapping("/{clientId}/orders/{orderId}/products")
    public ResponseEntity<?> getCustomerOrderProducts(@PathVariable Long clientId, @PathVariable Long orderId) {
        String ids = clientId.toString() + "," + orderId.toString();

        if (customerService.getClientById(clientId) == null) {
            return ResponseEntity.ok("Client with id " + clientId + " does not exists");
        }

        rabbitMQSender.sendClientIdAndOrderId(ids);

        // Attendre la réception du message. Vous pouvez implémenter un mécanisme d'attente ou de délai ici.
        try {
            Thread.sleep(1000); // Attendre 1 seconde pour que le message soit reçu. Ajustez selon vos besoins.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String commandOfClient = rabbitMQReceiver.getReceivedMessage();
        return ResponseEntity.ok(commandOfClient);
    }

}
