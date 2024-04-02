package com.service.impl;

import com.mapper.TransactionMapper;
import com.messages.NotificationMessage;
import com.model.*;
import com.model.dto.TransactionDto;
import com.model.dto.FilterDto;
import com.model.enumerations.NotificationType;
import com.model.enumerations.Type;
import com.model.exceptions.ResourceNotFoundException;
import com.repository.TransactionRepository;
import com.service.RabbitMqService;
import com.service.TransactionService;
import com.service.BudgetService;
import com.service.TransactionTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final BudgetService budgetService;

    private final TransactionTypeService transactionTypeService;

    private final RabbitMqService rabbitMqService;

    @Override
    public Transaction getTransactionById(Long id) {
        return this.transactionRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Transaction", "id", id.toString()));
    }

    @Override
    public List<Transaction> getAll() {
        return this.transactionRepository.findAll();
    }

    @Override
    public Transaction addTransaction(TransactionDto transactionDto, User user) {
        TransactionType transactionType = this.transactionTypeService
                .findByCategoryAndType(transactionDto.getCategoryId(), transactionDto.getType());

        LocalDate date = transactionDto.getDate();

        if (!budgetService.existsByMonthAndYear(date.getMonth(), date.getYear(), user)) {
            throw new ResourceNotFoundException("Budget", "chosen month and year", "");
        }

        Transaction transaction = TransactionMapper.mapToTransaction(transactionDto, new Transaction(), user, transactionType);

        Budget budget = budgetService.updateBudgetAfterAddTransaction(transaction);

        if (budget.getBalance() <= 0) {
           sendMessageToNotificationService(budget.getUser().getId(), NotificationType.LOW_BUDGET, budget.getMonth());
        }

        return this.transactionRepository.save(transaction);
    }

    @Override
    public Transaction update(Long id, TransactionDto transactionDto) {
        Transaction transaction = this.getTransactionById(id);
        Double amount = transactionDto.getAmount();
        String name = transactionDto.getName();
        String currency = transactionDto.getCurrency();

        TransactionType transactionType = this.transactionTypeService
                .findByCategoryAndType(transactionDto.getCategoryId(), transactionDto.getType());


        if (Objects.nonNull(amount)) {
            budgetService.updateBudgetAfterUpdateTransaction(transaction, transactionDto);
            transaction.setAmount(amount);
        }

        if (Objects.nonNull(name)) {
            transaction.setName(name);
        }

        if (Objects.nonNull(transactionType)) {
            transaction.setTransactionType(transactionType);
        }

        if (Objects.nonNull(currency)) {
            transaction.setCurrency(currency);
        }

        return this.transactionRepository.save(transaction);
    }

    @Override
    public Long delete(Long id) {
        Transaction transaction = this.getTransactionById(id);

        Budget budget = budgetService.updateBudgetAfterDeleteTransaction(transaction);
        this.transactionRepository.delete(transaction);

        sendMessageToNotificationService(transaction.getUser().getId(), NotificationType.TRANSACTION_DELETE, budget.getMonth());

        if (budget.getBalance() <= 0) {
            sendMessageToNotificationService(budget.getUser().getId(), NotificationType.LOW_BUDGET, budget.getMonth());
        }

        return transaction.getId();
    }

    @Override
    public List<Transaction> getTransactionsByUser(User user) {
        return this.transactionRepository.findByUser(user);
    }

    @Override
    public List<Transaction> filterTransactions(User user, FilterDto filterDto) {
        List<Transaction> transactions = this.transactionRepository.findByUser(user);

        if (!filterDto.getType().isEmpty()) {
            transactions = this.transactionRepository.findByUserAndTransactionType_Type(user, Type.valueOf(filterDto.getType()));
        }

        if (!filterDto.getMonth().isEmpty()) {
            transactions = this.filterByMonth(transactions, filterDto.getMonth());
        }

        if (filterDto.getYear() != 0) {
            transactions = this.getTransactionsByYear(transactions, filterDto.getYear());
        }

        return transactions;
    }

    @Override
    public List<Transaction> getTransactionsByDate(User user, FilterDto filterDto) {
        return this.transactionRepository.findByUserAndDate(user, filterDto.getDate());
    }

    @Override
    public List<Transaction> getTransactionsByType(User user, Type type) {
        return this.transactionRepository.findByUserAndTransactionType_Type(user, type);
    }

    @Override
    public List<Transaction> getTransactionsByTypeAndCategory(User user, Category category, Type type) {
        return this.transactionRepository.findByUserAndTransactionType_CategoryAndTransactionType_Type(user, category, type);
    }

    @Override
    public List<Transaction> getTransactionsByMonthAndYear(List<Transaction> transactions, String month, int year) {
        return transactions.stream()
                .filter(transaction ->
                        transaction.getDate().getYear() == year &&
                                transaction.getDate().getMonth().toString().equals(month)).toList();
    }

    @Override
    public List<Transaction> getTransactionsByMonthAndYearAndType(User user, String month, int year, Type type) {
        List<Transaction> transactions = this.transactionRepository
                .findByUserAndTransactionType_Type(user, type);
        transactions = this.getTransactionsByMonthAndYear(transactions, month, year);
        return transactions;
    }

    @Override
    public List<Transaction> getTransactionsByYear(List<Transaction> transactions, int year) {
        return transactions.stream()
                .filter(transaction -> transaction.getDate().getYear() == year).toList();
    }

    private List<Transaction> filterByMonth(List<Transaction> transactions, String month) {
        return transactions.stream()
                .filter(transaction -> transaction.getDate().getMonth().toString().equals(month)).toList();
    }

    private Double sum(List<Transaction> transactions) {
        return transactions.stream().map(Transaction::getAmount).reduce((double) 0, Double::sum);
    }

    private void sendMessageToNotificationService(Long userId, NotificationType type, Month month) {
        NotificationMessage notificationMessage = NotificationMessage.builder()
                .userId(userId)
                .notificationType(type)
                .month(month.getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                .build();

        rabbitMqService.sendMessageToNotificationService(notificationMessage);
    }
}
