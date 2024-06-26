package org.openexchangerates.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageReceived {

    private UUID id;

    private LocalDateTime date;

    private ExchangeRateDto payload;

}

