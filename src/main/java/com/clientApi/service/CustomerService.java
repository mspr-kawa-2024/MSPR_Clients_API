package com.clientApi.service;

import com.clientApi.model.Customer;
import com.clientApi.config.RabbitMQReceiver;
import com.clientApi.config.RabbitMQSender;
import com.clientApi.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * A class to manage the logic of the application
 */
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    @Autowired
    private RabbitMQReceiver rabbitMQReceiver;
    @Autowired
    private RabbitMQSender rabbitMQSender;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * get all the users form the database
     * @return all the users form the database
     */
    public List<Customer> getClients() {
        return customerRepository.findAll();
    }

    public Map<String, String> authenticateClient(String email, String password) {
        Map<String, String> response = new HashMap<>();

        Optional<Customer> clientOptional = customerRepository.findByEmailAndPassword(email, password);

        if (clientOptional.isPresent()) {
            response.put("message", "Authentication success");
        } else {
            response.put("message", "Authentication failed");
        }
        return response;
    }

    public Customer getClientById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public void addNewClient(Customer customer) {
        Optional<Customer> clientByEmail = customerRepository.findByEmail(customer.getEmail());
        if (clientByEmail.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        customerRepository.save(customer);
    }

    public void deleteClient(Long clientId) {
        boolean exists = customerRepository.existsById(clientId);
        if (!exists) {
            throw new IllegalStateException( "Client with id " + clientId + " does not exists");
        }
        customerRepository.deleteById(clientId);
    }
    @Transactional
    public Customer updateClient(Long clientId, String name, String lastname, String email) {
        Customer customer = customerRepository.findById(clientId)
                .orElseThrow(() -> new IllegalStateException("Client with id " + clientId + " does not exists"));

        boolean isUpdated = false;

        if (name != null && !name.isEmpty() && !Objects.equals(customer.getFirstName(), name)){
            customer.setFirstName(name);
            isUpdated = true;
        }

        if (lastname != null && !lastname.isEmpty() && !Objects.equals(customer.getLastName(), lastname)){
            customer.setLastName(lastname);
            isUpdated = true;
        }

        if (email != null && !email.isEmpty() && !Objects.equals(customer.getEmail(), email)){
            Optional<Customer> clientOptional = customerRepository.findByEmail(email);
            if (clientOptional.isPresent() && !clientOptional.get().getId().equals(customer.getId())) {
                throw new IllegalStateException("email taken");
            }
            customer.setEmail(email);
            isUpdated = true;
        }

        if (isUpdated) {
            customerRepository.save(customer);
        }

        return customer;
    }
/*
    @Transactional
    public void updateClient(Long clientId, String name, String lastname, String email) {
        Customer customer = customerRepository.findById(clientId)
                .orElseThrow(() -> new IllegalStateException("Client with id " + clientId + " does not exists"));

        boolean isUpdated = false;

        if (name != null && !name.isEmpty() && !Objects.equals(customer.getFirstName(), name)){
            customer.setFirstName(name);
            isUpdated = true;
        }

        if (lastname != null && !lastname.isEmpty() && !Objects.equals(customer.getLastName(), lastname)){
            customer.setLastName(lastname);
            isUpdated = true;
        }

        if (email != null && !email.isEmpty() && !Objects.equals(customer.getEmail(), email)){
            Optional<Customer> clientOptional = customerRepository.findByEmail(email);
            if (clientOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            customer.setEmail(email);
            isUpdated = true;
        }

        if (isUpdated) {
            customerRepository.save(customer);
        }
    }*/


    @RabbitListener(queues = "clientIdsIdQueue")
    public void verificationOfClientsToAddInOrder(String clientIdsReceived) {
        List<Long> ids = convertirEnLongs(clientIdsReceived);

        int i = 0;
        Long idToSend = null;
        for (Long id : ids) {
            if (!customerRepository.existsById(id)) {
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

