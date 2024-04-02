package org.notificationservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.notificationservice.messages.NotificationMessageResponse;
import org.notificationservice.model.Notification;
import org.notificationservice.service.RabbitMqService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMqServiceImpl implements RabbitMqService {

    @Value("${budgetplanner.exchange}")
    private String budgetPlannerExchange;

    @Value("${notification.received.routing.key}")
    private String notificationReceivedRoutingKey;

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
    public void sendMessageToBudgetPlanner(NotificationMessageResponse messageResponse) {
        sendMessage(notificationReceivedRoutingKey, messageResponse);
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
