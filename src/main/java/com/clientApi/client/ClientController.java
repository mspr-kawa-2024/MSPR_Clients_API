package com.clientApi.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/client")
@CrossOrigin(origins = "http://localhost:3000",
        methods = {RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE})
public class ClientController {

    private final ClientService clientService;
    private final CommandeService commandeService;

    @Autowired
    public ClientController(ClientService clientService, CommandeService commandeService) {
        this.clientService = clientService;
        this.commandeService = commandeService;
    }

    @GetMapping
    public List<Client> getClients() {
        return clientService.getClients();
    }

    @PostMapping
    public void registerNewClient(@RequestBody Client client) {
        clientService.addNewClient(client);
    }

    @PutMapping(path = "{clientId}")
    public void updateClient(@PathVariable("clientId") Long clientId,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) String email){
        clientService.updateClient(clientId, name, email);
    }

    @DeleteMapping(path = "{clientId}")
    public void deleteClient(@PathVariable("clientId") Long clientId){
        clientService.deleteClient(clientId);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticateClient(@RequestBody ClientCredentials clientCredentials) {
        Map<String, String> jsonSend = clientService.authenticateClient(clientCredentials.getEmail(), clientCredentials.getPassword());

        if (jsonSend != null && jsonSend.containsKey("message")) {
            return ResponseEntity.ok(jsonSend);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonSend);
        }
    }

    @GetMapping("/command/{id}")
    public ResponseEntity<Map<String, Object>> getCommande(@PathVariable Long id) {
        Map<String, Object> commande = commandeService.getCommandeById(id);
        if (commande != null) {
            return ResponseEntity.ok(commande);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/commands")
    public ResponseEntity<List<Map<String, Object>>> getCommands() {
        List<Map<String, Object>> commandes = commandeService.getAllCommandes();
        if (commandes != null) {
            return ResponseEntity.ok(commandes);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
