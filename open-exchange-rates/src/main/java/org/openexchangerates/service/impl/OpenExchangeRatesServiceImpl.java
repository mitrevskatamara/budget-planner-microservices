package org.openexchangerates.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.openexchangerates.config.OpenExchangeRatesConfig;
import org.openexchangerates.dto.ExchangeRatesResponse;
import org.openexchangerates.messages.ExchangeRateDto;
import org.openexchangerates.service.OpenExchangeRatesService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.openexchangerates.utils.Constants.*;

@Service
@Slf4j
public class OpenExchangeRatesServiceImpl implements OpenExchangeRatesService {

    private final OpenExchangeRatesConfig config;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public OpenExchangeRatesServiceImpl(OpenExchangeRatesConfig config, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.config = config;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public ExchangeRatesResponse getExchangeRates(ExchangeRateDto exchangeRateDto) {
        String appId = config.getAppId();

        String url = API_URL  + "?app_id=" + appId + "&symbols=" + exchangeRateDto.getRate();

        String response = restTemplate.getForObject(url, String.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(response).get("rates");
            String currencyCode = jsonNode.fieldNames().next();
            double exchangeRate = jsonNode.get(currencyCode).asDouble();

            return new ExchangeRatesResponse(exchangeRateDto.getUserId(), currencyCode, exchangeRate);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
