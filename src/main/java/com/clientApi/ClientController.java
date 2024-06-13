package com.clientApi;

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
public class ClientController {

    private final ClientService clientService;

    @Autowired
    private RabbitMQSender rabbitMQSender;

    @Autowired
    private RabbitMQReceiver rabbitMQReceiver;

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
    public ResponseEntity<?> getCustomerOrderProducts(@PathVariable Long clientId, @PathVariable Long orderId) {
        String ids = clientId.toString() + "," + orderId.toString();

        if (clientService.getClientById(clientId) == null) {
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
