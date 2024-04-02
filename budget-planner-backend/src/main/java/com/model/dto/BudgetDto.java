package com.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BudgetDto {

    private Long id;

    private String month;

    private int year;

    private Double budget;

    private String username;

    private String currency;

    public Double balance;

}