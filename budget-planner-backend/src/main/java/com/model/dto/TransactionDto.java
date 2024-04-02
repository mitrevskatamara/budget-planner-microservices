package com.model.dto;

import com.model.enumerations.Type;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@Builder
public class TransactionDto {

    private Type type;

    private String name;

    private Long categoryId;

    private Double amount;

    private LocalDate date;

    private String username;

    private String currency;
}