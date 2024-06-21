package com.clientApi.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RabbitMQConfigTest {

    @Autowired
    private Queue orderQueue;

    @Autowired
    private Queue responseQueue;

    @Autowired
    private Queue orderProductQueue;

    @Autowired
    private Queue clientIdsIdQueue;

    @Autowired
    private Queue responseClientIdsVerificationQueue;

    @Test
    void testOrderQueue() {
        assertNotNull(orderQueue);
        assertEquals(RabbitMQConfig.ORDER_QUEUE_NAME, orderQueue.getName());
    }

    @Test
    void testResponseQueue() {
        assertNotNull(responseQueue);
        assertEquals(RabbitMQConfig.RESPONSE_QUEUE_NAME, responseQueue.getName());
    }

    @Test
    void testOrderProductQueue() {
        assertNotNull(orderProductQueue);
        assertEquals(RabbitMQConfig.ORDER_PRODUCT_QUEUE_NAME, orderProductQueue.getName());
    }

    @Test
    void testClientIdsIdQueue() {
        assertNotNull(clientIdsIdQueue);
        assertEquals("clientIdsIdQueue", clientIdsIdQueue.getName());
    }

    @Test
    void testResponseClientIdsVerificationQueue() {
        assertNotNull(responseClientIdsVerificationQueue);
        assertEquals("responseClientIdsVerificationQueue", responseClientIdsVerificationQueue.getName());
    }

    @Configuration
    static class TestConfig {

        @Bean
        public RabbitMQConfig rabbitMQConfig() {
            return new RabbitMQConfig();
        }

        @Bean
        public Queue orderQueue(RabbitMQConfig rabbitMQConfig) {
            return rabbitMQConfig.orderQueue();
        }

        @Bean
        public Queue responseQueue(RabbitMQConfig rabbitMQConfig) {
            return rabbitMQConfig.responseQueue();
        }

        @Bean
        public Queue orderProductQueue(RabbitMQConfig rabbitMQConfig) {
            return rabbitMQConfig.orderProductQueue();
        }

        @Bean
        public Queue clientIdsIdQueue(RabbitMQConfig rabbitMQConfig) {
            return rabbitMQConfig.clientIdsIdQueue();
        }

        @Bean
        public Queue responseClientIdsVerificationQueue(RabbitMQConfig rabbitMQConfig) {
            return rabbitMQConfig.responseClientIdsVerificationQueue();
        }
    }
}
