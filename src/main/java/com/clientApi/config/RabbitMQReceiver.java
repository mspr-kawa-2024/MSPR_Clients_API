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

    private String receivedMessage;
    private String receivedMessageForClientIds;

    @RabbitListener(queues = "orderToSendQueue")
    public void receiveOrderOfClient(String message) {
        this.receivedMessage = message;
    }

    public String getReceivedMessage() {
        return this.receivedMessage;
    }
}
