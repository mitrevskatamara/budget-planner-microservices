package org.openexchangerates.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.openexchangerates.dto.ExchangeRatesResponse;
import org.openexchangerates.messages.MessageReceived;
import org.openexchangerates.service.OpenExchangeRatesService;
import org.openexchangerates.service.RabbitMqService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class OpenExchangeRatesListener {

    private final RabbitMqService rabbitMqService;

    private final ObjectMapper objectMapper;

    public OpenExchangeRatesListener(RabbitMqService rabbitMqService, ObjectMapper objectMapper) {
        this.rabbitMqService = rabbitMqService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = {"open_exchange_rates_queue"})
    public void receiveMessage(Message message) {
        try {
            log.info("Received message!");
            String content = new String(message.getBody(), StandardCharsets.UTF_8);
            log.info("Message: " + content);

            MessageReceived messageReceived = objectMapper.readValue(content, MessageReceived.class);
            rabbitMqService.sendMessageWithExchangeRateResponse(messageReceived.getPayload());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
