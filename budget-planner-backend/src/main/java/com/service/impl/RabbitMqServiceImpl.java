package com.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.messages.ExchangeRateMessage;
import com.messages.Message;
import com.messages.NotificationMessage;
import com.service.RabbitMqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMqServiceImpl implements RabbitMqService {

    @Value("${budgetplanner.exchange}")
    private String budgetPlannerExchange;

    @Value("${openexchangerates.routingkey}")
    private String openExchangeRatesRoutingKey;

    @Value("${notificationservice.routingkey}")
    private String notificationServiceRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    public RabbitMqServiceImpl(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> void sendMessage(String routingKey, T object) {
        send(routingKey, object);
    }

    @Override
    public void sendMessageToGetRate(ExchangeRateMessage exchangeRateMessage) {
        Message<ExchangeRateMessage> message = new Message<>(exchangeRateMessage);

        log.info("Message is sent to: " + openExchangeRatesRoutingKey);
        sendMessage(openExchangeRatesRoutingKey, message);
    }

    @Override
    public void sendMessageToNotificationService(NotificationMessage notificationMessage) {
        Message<NotificationMessage> message = new Message<>(notificationMessage);

        log.info("Message is sent to: " + notificationServiceRoutingKey);
        sendMessage(notificationServiceRoutingKey, message);
    }

    private <T> void send(String routingKey, T message) {
        try {
            log.info("Message: " + message);
            rabbitTemplate.convertAndSend(budgetPlannerExchange, routingKey, objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

    }
}
