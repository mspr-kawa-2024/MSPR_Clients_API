package com.clientApi.client;

import com.clientApi.CustomerApplication;
import com.clientApi.config.RabbitMQSender;
import com.clientApi.model.Customer;
import com.clientApi.service.CustomerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CustomerApplication.class)
@ActiveProfiles("test")
@Testcontainers
public class RabbitMQIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQSender rabbitMQSender;

    @Mock
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
        Object receivedMessage = rabbitTemplate.receiveAndConvert("orderQueue");
        assertEquals(message, receivedMessage);
    }
}
