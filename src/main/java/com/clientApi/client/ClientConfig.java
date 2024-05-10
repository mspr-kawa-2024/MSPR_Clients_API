package com.clientApi.client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class ClientConfig {

    @Bean
    CommandLineRunner commandLineRunner(ClientRepository repository)  {
        // Have access to our repository
        return args -> {
            Client mohamed = new Client(
                    "Mohamed",
                    "Amine",
                    "med@gmail.com",
                    "password",
                    LocalDate.of(2023, Month.DECEMBER, 1),
                    LocalDate.of(2024, Month.JANUARY, 31)
            );

            Client anthonny = new Client(
                    "Anthonny",
                    "AnthonnyLastName",
                    "Anthonny@gmail.com",
                    "passwordAnthonny",
                    LocalDate.of(2023, Month.DECEMBER, 1),
                    LocalDate.of(2024, Month.JANUARY, 31)
            );

            // Save Clients into Database
            repository.saveAll(
                    List.of(mohamed, anthonny)
            );
        };
    }

}
