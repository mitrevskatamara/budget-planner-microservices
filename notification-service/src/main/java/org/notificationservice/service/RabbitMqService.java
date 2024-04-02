package org.notificationservice.service;

import org.notificationservice.messages.NotificationMessageResponse;
import org.notificationservice.model.Notification;

public interface RabbitMqService {

    <T> void sendMessage(String routingKey, T object);

    void sendMessageToBudgetPlanner(NotificationMessageResponse messageResponse);

}
