package org.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.notificationservice.model.dto.MessageReceived;
import org.notificationservice.service.NotificationService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;


@Slf4j
@Component
@AllArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;

    private final ObjectMapper objectMapper;

    @RabbitListener(queues = {"notification_service_queue"})
    public void receiveMessage(Message message) {
        try {
            log.info("Received message!");
            String content = new String(message.getBody(), StandardCharsets.UTF_8);
            log.info("Message: " + content);

            MessageReceived messageReceived = objectMapper.readValue(content, MessageReceived.class);
            notificationService.createNotification(messageReceived.getPayload());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

    }
}
