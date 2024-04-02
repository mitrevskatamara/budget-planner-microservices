package org.openexchangerates.service;

import org.openexchangerates.dto.ExchangeRatesResponse;
import org.openexchangerates.messages.ExchangeRateDto;


public interface OpenExchangeRatesService {

    ExchangeRatesResponse getExchangeRates(ExchangeRateDto exchangeRateDto);
}
