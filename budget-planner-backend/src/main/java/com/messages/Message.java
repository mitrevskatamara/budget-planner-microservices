package com.messages;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Message<T> {

    private UUID id = UUID.randomUUID();

    private LocalDateTime date = LocalDateTime.now();

    private T payload;

    public Message(T payload) {
        this.payload = payload;
    }
}
