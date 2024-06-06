package com.clientApi.client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OrderConfig {

    @Bean
    CommandLineRunner commandLineRunner(OrderRepository repository)  {
        // Have access to our repository
        Client client = new Client();
        Client client2 = new Client();
        return args -> {
            Order order1 = new Order("1");

            Order order2 = new Order("1");

            Order order3 = new Order("2");

            // Save Clients into Database
            repository.saveAll(
                    List.of(order1, order2, order3)
            );
        };
    }

}
