package com.service;

import com.model.Category;
import com.model.Transaction;
import com.model.User;
import com.model.dto.TransactionDto;
import com.model.dto.FilterDto;
import com.model.enumerations.Type;

import java.util.List;

public interface TransactionService {

    Transaction getTransactionById(Long id);

    List<Transaction> getAll();

    Transaction addTransaction(TransactionDto transactionDto, User user);

    Transaction update(Long id, TransactionDto transactionDto);

    Long delete(Long id);

    List<Transaction> getTransactionsByUser(User user);

    List<Transaction> filterTransactions(User user, FilterDto filterDto);

    List<Transaction> getTransactionsByDate(User user, FilterDto filterDto);

    List<Transaction> getTransactionsByType(User user, Type type);

    List<Transaction> getTransactionsByTypeAndCategory(User user, Category category, Type type);

    List<Transaction> getTransactionsByMonthAndYear(List<Transaction> transactions, String month, int year);

    List<Transaction> getTransactionsByMonthAndYearAndType(User user, String month, int year, Type type);

    List<Transaction> getTransactionsByYear(List<Transaction> transactions, int year);
}