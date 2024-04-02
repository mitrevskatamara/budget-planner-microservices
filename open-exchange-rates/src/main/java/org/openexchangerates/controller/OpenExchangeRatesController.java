package org.openexchangerates.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.openexchangerates.dto.ExchangeRatesResponse;
import org.openexchangerates.messages.ExchangeRateDto;
import org.openexchangerates.service.OpenExchangeRatesService;
import org.openexchangerates.service.RabbitMqService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/api/openExchangeRates")
public class OpenExchangeRatesController {

    private final OpenExchangeRatesService openExchangeRatesService;

    private final RabbitMqService rabbitMqService;

    @GetMapping("/getRates")
    public ResponseEntity<ExchangeRatesResponse> getExchangeRates(@RequestBody ExchangeRateDto exchangeRateDto) {
        return new ResponseEntity<>(openExchangeRatesService.getExchangeRates(exchangeRateDto), HttpStatus.OK);
    }

    @PostMapping("/rabbitMq")
    public void rabbitMq() {
        this.rabbitMqService.sendMessage("budget_planner_routing_key", "test message");
    }

}
