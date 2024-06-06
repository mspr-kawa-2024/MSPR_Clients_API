package com.clientApi.client;

import com.clientApi.config.RabbitMQConfig;
import com.clientApi.config.RabbitMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/client")
@CrossOrigin(origins = "http://localhost:3000",
        methods = {RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE})
public class ClientController {

    private final ClientService clientService;

    @Autowired
    private RabbitMQSender rabbitTemplate;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> getClients() {
        return clientService.getClients();
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<Client> getClientById(@PathVariable Long clientId) {
        Client client = clientService.getClientById(clientId);
        if (client != null) {
            return ResponseEntity.ok(client);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public void registerNewClient(@RequestBody Client client) {
        clientService.addNewClient(client);
    }

    @PutMapping(path = "{clientId}")
    public void updateClient(@PathVariable("clientId") Long clientId,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) String email) {
        clientService.updateClient(clientId, name, email);
    }

    @DeleteMapping(path = "{clientId}")
    public void deleteClient(@PathVariable("clientId") Long clientId) {
        clientService.deleteClient(clientId);
    }

    @GetMapping("/{clientId}/orders/{orderId}/products")
    public void getCustomerOrderProducts(@PathVariable Long clientId, @PathVariable Long orderId) {
        String ids = clientId + "," + orderId;
        rabbitTemplate.sendClientIdAndOrderId(ids);
        //List<?> response = (List<?>) rabbitTemplate.receiveAndConvert(RabbitMQConfig.RESPONSE_QUEUE_NAME);
        //System.out.println(response);
        //return ResponseEntity.ok(response);
    }
}
