package com.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatisticsDto {

    private Double expense;

    private Double income;

    private String month;

    private int year;

    private Long userId;

    private String username;

    private Double sumOfExpenses;

    private Double sumOfIncomes;

    private int numberOfIncomes;

    private int numberOfExpenses;
}