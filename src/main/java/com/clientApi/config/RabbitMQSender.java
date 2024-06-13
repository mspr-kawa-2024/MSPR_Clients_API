package com.clientApi.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class RabbitMQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendClientIdAndOrderId(String orderId) {
        rabbitTemplate.convertAndSend("orderQueue", orderId);
    }

    public void sendResponseOfIdsVerification(String response) {
        rabbitTemplate.convertAndSend("responseClientIdsVerificationQueue", response);
    }
}

