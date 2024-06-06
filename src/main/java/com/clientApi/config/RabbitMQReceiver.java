package com.clientApi.config;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class RabbitMQReceiver {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "orderQueue")
    public String receiveProductInOrder() {
        Object response = rabbitTemplate.convertSendAndReceive("responseQueue");
        return response != null ? response.toString() : null;
    }

}

