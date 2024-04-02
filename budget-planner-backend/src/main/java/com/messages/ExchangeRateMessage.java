package com.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeRateMessage {

    private String rate;

    private Long userId;
}
