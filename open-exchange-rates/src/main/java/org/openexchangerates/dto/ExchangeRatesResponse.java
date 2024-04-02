package org.openexchangerates.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeRatesResponse {

    private String userId;

    private String currencyCode;

    private double exchangeRate;
}

