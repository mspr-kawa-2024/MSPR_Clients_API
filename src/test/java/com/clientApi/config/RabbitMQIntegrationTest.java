package com.clientApi.config;

import com.clientApi.config.RabbitMQSender;
import com.clientApi.model.Customer;
import com.clientApi.service.CustomerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public class RabbitMQIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQSender rabbitMQSender;

    @MockBean
    private CustomerService customerService;

    @Container
    public static GenericContainer<?> rabbitmq = new GenericContainer<>("rabbitmq:3-management")
            .withExposedPorts(5672, 15672);

    @BeforeAll
    static void setUp() {
        System.setProperty("spring.rabbitmq.host", rabbitmq.getHost());
        System.setProperty("spring.rabbitmq.port", rabbitmq.getMappedPort(5672).toString());
    }

    @Test
    void testSendAndReceive() {
        // Mocking the customerService to avoid database interactions
        Customer mockCustomer = new Customer(1L, "John", "Doe", "john.doe@example.com", "password", null, null);
        when(customerService.getClientById(anyLong())).thenReturn(mockCustomer);

        String message = "Test message";
        rabbitMQSender.sendClientIdAndOrderId(message);

        // Waiting a short period to ensure the message is sent and received
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted state
            throw new IllegalStateException(e); // Throw a more appropriate exception
        }

        Object receivedMessage = rabbitTemplate.receiveAndConvert("orderQueue");
        assertEquals(message, receivedMessage);
    }
}
