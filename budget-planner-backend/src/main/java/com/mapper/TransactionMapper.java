package com.mapper;

import com.model.Transaction;
import com.model.TransactionType;
import com.model.User;
import com.model.dto.TransactionDto;

public class TransactionMapper {

    public static Transaction mapToTransaction(TransactionDto transactionDto, Transaction transaction, User user,
                                               TransactionType transactionType) {
        transaction.setName(transactionDto.getName());
        transaction.setUser(user);
        transaction.setDate(transactionDto.getDate());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setCurrency(transactionDto.getCurrency());
        transaction.setTransactionType(transactionType);

        return transaction;
    }

}
