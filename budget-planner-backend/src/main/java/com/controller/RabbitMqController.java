package com.controller;

import com.messages.ExchangeRateMessage;
import com.service.RabbitMqService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rabbitMq")
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
@Tag(name = "Role Controller", description = "Endpoints for handling requests for messaging with RabbitMq")
public class RabbitMqController {

    private final RabbitMqService rabbitMqService;

    @Operation(summary = "Endpoint to send message to Open exchange rate service to get rate")
    @PostMapping("/getRate")
    public void getExchangeRates(@RequestParam String rate, @RequestParam Long userId) {
        ExchangeRateMessage exchangeRateMessage = new ExchangeRateMessage(rate, userId);
        rabbitMqService.sendMessageToGetRate(exchangeRateMessage);
    }

}
