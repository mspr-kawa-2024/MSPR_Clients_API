package com.clientApi.config;

import com.clientApi.Client;
import com.clientApi.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class ClientConfig {

    @Bean
    CommandLineRunner commandLineRunner(ClientRepository repository)  {
        // Have access to our repository
        return args -> {

            Client client1 = new Client(
                    "firstName1",
                    "lastName1",
                    "email1@gmail.com",
                    "password1",
                    LocalDate.of(2023, Month.DECEMBER, 1),
                    LocalDate.of(2024, Month.JANUARY, 31)
            );

            Client client2 = new Client(
                    "firstName2",
                    "lastName2",
                    "email2@gmail.com",
                    "password2",
                    LocalDate.of(2023, Month.DECEMBER, 1),
                    LocalDate.of(2024, Month.JANUARY, 31)
            );

            Client client3 = new Client(
                    "firstName3",
                    "lastName3",
                    "email3@gmail.com",
                    "password3",
                    LocalDate.of(2023, Month.DECEMBER, 1),
                    LocalDate.of(2024, Month.JANUARY, 31)
            );

            Client client4 = new Client(
                    "firstName4",
                    "lastName4",
                    "email4@gmail.com",
                    "password4",
                    LocalDate.of(2023, Month.DECEMBER, 1),
                    LocalDate.of(2024, Month.JANUARY, 31)
            );

            // Save Clients into Database
            repository.saveAll(
                    List.of(client1, client2, client3, client4)
            );
        };
    }

    // Configuration de RestTemplate
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
