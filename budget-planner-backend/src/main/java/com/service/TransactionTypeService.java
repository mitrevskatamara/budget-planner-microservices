package com.service;

import com.model.Category;
import com.model.TransactionType;
import com.model.dto.TransactionTypeDto;
import com.model.enumerations.Type;

public interface TransactionTypeService {

    TransactionType getTransactionTypeById(Long id);

    TransactionType create(TransactionTypeDto transactionTypeDto);

    TransactionType update(Long id, TransactionTypeDto transactionTypeDto);

    TransactionType delete(Long id);

    TransactionType findByCategoryAndType(Long categoryId, Type type);
}