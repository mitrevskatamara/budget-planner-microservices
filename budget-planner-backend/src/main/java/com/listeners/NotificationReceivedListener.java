package com.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class NotificationReceivedListener {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationReceivedListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = {"notification_received_queue"})
    public void receiveMessage(Message message) {
        log.info("Received message!");
        String content = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("Message: " + content);

        messagingTemplate.convertAndSend("/topic/notification", content);
    }
}
