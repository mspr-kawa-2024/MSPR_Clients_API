package com.clientApi.config;

import com.clientApi.config.RabbitMQReceiver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RabbitMQReceiverTest {

    @InjectMocks
    private RabbitMQReceiver rabbitMQReceiver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReceiveOrderOfClient() {
        String message = "orderMessage";
        rabbitMQReceiver.receiveOrderOfClient(message);
        assertEquals(message, rabbitMQReceiver.getReceivedMessage());
    }
}
