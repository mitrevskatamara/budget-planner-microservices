package org.openexchangerates.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.openexchangerates.dto.ExchangeRatesResponse;
import org.openexchangerates.messages.ExchangeRateDto;
import org.openexchangerates.service.OpenExchangeRatesService;
import org.openexchangerates.service.RabbitMqService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMqServiceImpl implements RabbitMqService {

    @Value("${budgetplanner.exchange}")
    private String budgetPlannerExchange;

    @Value("${budgetplanner.routingkey}")
    private String budgetPlannerRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    private final OpenExchangeRatesService openExchangeRatesService;

    public RabbitMqServiceImpl(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper, OpenExchangeRatesService openExchangeRatesService) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.openExchangeRatesService = openExchangeRatesService;
    }

    @Override
    public <T> void sendMessage(String routingKey, T object) {
        send(routingKey, object);
    }

    @Override
    public void sendMessageWithExchangeRateResponse(ExchangeRateDto exchangeRateDto) {
        log.info("Message is sent to: " + budgetPlannerRoutingKey);
        ExchangeRatesResponse exchangeRatesResponse = openExchangeRatesService.getExchangeRates(exchangeRateDto);

        sendMessage(budgetPlannerRoutingKey, exchangeRatesResponse);
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
