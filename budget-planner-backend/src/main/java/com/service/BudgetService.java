package com.service;

import com.model.Budget;
import com.model.Transaction;
import com.model.User;
import com.model.dto.BudgetDto;
import com.model.dto.FilterDto;
import com.model.dto.TransactionDto;

import java.time.Month;
import java.util.List;

public interface BudgetService {

    List<BudgetDto> getAll();

    Budget getBudgetById(Long id);

    BudgetDto create(User user, BudgetDto budgetDto);

    BudgetDto update(Long id, BudgetDto budgetDto);

    Budget updateBudgetAfterAddTransaction(Transaction transaction);

    Budget updateBudgetAfterDeleteTransaction(Transaction transaction);

    Budget updateBudgetAfterUpdateTransaction(Transaction transaction, TransactionDto transactionDto);

    Budget findByUserAndMonthAndYear(User user, Month month, int year);

    List<BudgetDto> findByUser(User user);

    Boolean existsByMonthAndYear(Month month, int year, User user);

    List<BudgetDto> filterBudgets(User user, FilterDto filterDto);
}
