package com.clientApi.config;

import com.clientApi.config.RabbitMQSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.verify;

public class RabbitMQSenderTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RabbitMQSender rabbitMQSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendClientIdAndOrderId() {
        String orderId = "1";
        rabbitMQSender.sendClientIdAndOrderId(orderId);
        verify(rabbitTemplate).convertAndSend("orderQueue", orderId);
    }

    @Test
    void testSendResponseOfIdsVerification() {
        String response = "response";
        rabbitMQSender.sendResponseOfIdsVerification(response);
        verify(rabbitTemplate).convertAndSend("responseClientIdsVerificationQueue", response);
    }
}
