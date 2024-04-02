package com.service;

import com.messages.ExchangeRateMessage;
import com.messages.NotificationMessage;

public interface RabbitMqService {

    <T> void sendMessage(String routingKey, T object);

    void sendMessageToGetRate(ExchangeRateMessage exchangeRateMessage);

    void sendMessageToNotificationService(NotificationMessage notificationMessage);
}
