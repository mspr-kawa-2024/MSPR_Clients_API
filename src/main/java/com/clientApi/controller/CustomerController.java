package com.clientApi.controller;

import com.clientApi.model.Customer;
import com.clientApi.service.CustomerService;
import com.clientApi.config.RabbitMQReceiver;
import com.clientApi.config.RabbitMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping(path = "api/v1/client")
@CrossOrigin(origins = "http://localhost:3000",
        methods = {RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE})
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    private RabbitMQSender rabbitMQSender;

    @Autowired
    private RabbitMQReceiver rabbitMQReceiver;


    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
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
    public void updateClient(@PathVariable("clientId") Long clientId,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) String lastname,
                             @RequestParam(required = false) String email) {
        customerService.updateClient(clientId, name, lastname, email);
    }

    @DeleteMapping(path = "{clientId}")
    public void deleteClient(@PathVariable("clientId") Long clientId) {
        customerService.deleteClient(clientId);
    }

    @GetMapping("/{clientId}/orders/{orderId}/products")
    public ResponseEntity<?> getCustomerOrderProducts(@PathVariable Long clientId, @PathVariable Long orderId) throws InterruptedException {
        String ids = clientId.toString() + "," + orderId.toString();

        if (customerService.getClientById(clientId) == null) {
            return ResponseEntity.ok("Client with id " + clientId + " does not exists");
        }

        rabbitMQSender.sendClientIdAndOrderId(ids);

        // Attendre la réception du message. Vous pouvez implémenter un mécanisme d'attente ou de délai ici.
        try {
            Thread.sleep(1000); // Attendre 1 seconde pour que le message soit reçu. Ajustez selon vos besoins.
        } catch (InterruptedException e) {
            throw new InterruptedException();
        }

        String commandOfClient = rabbitMQReceiver.getReceivedMessage();
        return ResponseEntity.ok(commandOfClient);
    }

}
