package com.clientApi;

import com.clientApi.Client;
import com.clientApi.ClientRepository;
import com.clientApi.config.RabbitMQReceiver;
import com.clientApi.config.RabbitMQSender;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * A class to manage the logic of the application
 */
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    @Autowired
    private RabbitMQReceiver rabbitMQReceiver;
    @Autowired
    private RabbitMQSender rabbitMQSender;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * get all the users form the database
     * @return all the users form the database
     */
    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    public Map<String, String> authenticateClient(String email, String password) {
        Map<String, String> response = new HashMap<>();

        Optional<Client> clientOptional = clientRepository.findByEmailAndPassword(email, password);

        if (clientOptional.isPresent()) {
            response.put("message", "Authentication success");
        } else {
            response.put("message", "Authentication failed");
        }
        return response;
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    public void addNewClient(Client client) {
        Optional<Client> clientByEmail = clientRepository.findByEmail(client.getEmail());
        if (clientByEmail.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        clientRepository.save(client);
    }

    public void deleteClient(Long clientId) {
        boolean exists = clientRepository.existsById(clientId);
        if (!exists) {
            throw new IllegalStateException( "Client with id " + clientId + " does not exists");
        }
        clientRepository.deleteById(clientId);
    }

    @Transactional  //pour ne pas utiliser des requete sql
    public void updateClient(Long clientId, String name, String email) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalStateException("Client with id " + clientId + " does not exists"));

        if (name != null && name.length() > 0 && !Objects.equals(client.getFirstName(), name)){
            client.setFirstName(name);
        }

        if (email != null && email.length() > 0 && !Objects.equals(client.getEmail(), email)){
            Optional<Client> clientOptional = clientRepository.findByEmail(email);
            if (clientOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            client.setEmail(email);
        }
    }

    @RabbitListener(queues = "clientIdsIdQueue")
    public void verificationOfClientsToAddInOrder(String clientIdsReceived) {
        List<Long> ids = convertirEnLongs(clientIdsReceived);

        int i = 0;
        Long idToSend = null;
        for (Long id : ids) {
            if (!clientRepository.existsById(id)) {
                i++;
                idToSend = id;
                break;
            }
        }
        if (i>0) {
            rabbitMQSender.sendResponseOfIdsVerification(idToSend.toString());
        } else {
            rabbitMQSender.sendResponseOfIdsVerification("ok");
        }
    }

    private static List<Long> convertirEnLongs(String input) {
        String[] elements = input.split(",");
        List<Long> result = new ArrayList<>();

        for (String element : elements) {
            element = element.trim();
            try {
                result.add(Long.parseLong(element));
            } catch (NumberFormatException e) {
                System.out.println("Erreur : '" + element + "' n'est pas un nombre valide.");
                return new ArrayList<>();
            }
        }
        return result;
    }
}

