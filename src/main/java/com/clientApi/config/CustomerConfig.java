package com.clientApi.config;

import com.clientApi.model.Customer;
import com.clientApi.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class CustomerConfig {

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository repository)  {

        String raw1 = "password1";
        String raw2 = "password2";
        String raw3 = "password3";
        String raw4 = "password4";
        String p1 = encoder.encode(raw1);
        String p2 = encoder.encode(raw2);
        String p3 = encoder.encode(raw3);
        String p4 = encoder.encode(raw4);

        // Have access to our repository
        return args -> {

            Customer customer1 = new Customer(
                    "firstName1",
                    "lastName1",
                    "email1@gmail.com",
                    p1,
                    LocalDate.of(2023, Month.DECEMBER, 1),
                    LocalDate.of(2024, Month.JANUARY, 31)
            );

            Customer customer2 = new Customer(
                    "firstName2",
                    "lastName2",
                    "email2@gmail.com",
                    p2,
                    LocalDate.of(2023, Month.DECEMBER, 1),
                    LocalDate.of(2024, Month.JANUARY, 31)
            );

            Customer customer3 = new Customer(
                    "firstName3",
                    "lastName3",
                    "email3@gmail.com",
                    p3,
                    LocalDate.of(2023, Month.DECEMBER, 1),
                    LocalDate.of(2024, Month.JANUARY, 31)
            );

            Customer customer4 = new Customer(
                    "firstName4",
                    "lastName4",
                    "email4@gmail.com",
                    p4,
                    LocalDate.of(2023, Month.DECEMBER, 1),
                    LocalDate.of(2024, Month.JANUARY, 31)
            );

            // Save Clients into Database
            repository.saveAll(
                    List.of(customer1, customer2, customer3, customer4)
            );
        };
    }

    // Configuration de RestTemplate
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
