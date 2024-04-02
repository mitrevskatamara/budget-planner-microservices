package com.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class FilterDto {

    private String month;

    private int year;

    private String type;

    private String username;

    private LocalDate date;
}