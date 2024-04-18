package com.clientApi.client;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A class to manage the logic of the application
 */
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * get all the users form the database
     * @return all the users form the database
     */
    public List<Client> getClients() {
        return List.of(
                new Client(
                        1L,
                        "Mohamed",
                        "Amine",
                        "med@gmail.com",
                        "password",
                        LocalDate.of(2023, Month.DECEMBER, 1),
                        LocalDate.of(2024, Month.JANUARY, 31)
                )
        );
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
}

