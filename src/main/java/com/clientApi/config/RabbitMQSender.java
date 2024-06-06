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

    public void sendClientIdAndOrderId(String clientIdAndOrderId) {
        rabbitTemplate.convertAndSend("orderProductQueue", clientIdAndOrderId);
    }

    public void sendOrderId(Long orderId) {
        rabbitTemplate.convertAndSend("orderQueue", orderId);
    }

    /*
    public CompletableFuture<Map<String, Object>> sendAndReceiveOrderId(Long orderId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Assuming that the RPC call returns a Map<String, Object>
                return (Map<String, Object>) rabbitTemplate.convertSendAndReceive("orderQueue", orderId);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }*/
}
