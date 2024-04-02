package com.model.dto;

import com.model.enumerations.Type;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionTypeDto {

    private Type type;

    private Long categoryId;
}