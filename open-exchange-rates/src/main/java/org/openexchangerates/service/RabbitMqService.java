package org.openexchangerates.service;

import org.openexchangerates.messages.ExchangeRateDto;

public interface RabbitMqService {

    <T> void sendMessage(String routingKey, T object);

    void sendMessageWithExchangeRateResponse(ExchangeRateDto exchangeRateDto);
}
