package com.service.impl;

import com.model.Category;
import com.model.Transaction;
import com.model.User;
import com.model.dto.StatisticsDto;
import com.model.enumerations.Type;
import com.service.CategoryService;
import com.service.StatisticsService;
import com.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final TransactionService transactionService;
    private final CategoryService categoryService;

    @Override
    public List<Double> getStatisticsForExpensesByCategory(User user, StatisticsDto statisticsDto) {
        List<Double> result = new ArrayList<>();
        List<Category> categories = this.categoryService.getAll();
        String month = statisticsDto.getMonth();
        int year = statisticsDto.getYear();

        List<Transaction> transactions = this.transactionService
                .getTransactionsByMonthAndYearAndType(user, month, year, Type.Expense);
        Double total = sum(transactions);

        for (Category category : categories) {
            List<Transaction> transactionsByTypeAndCategory = this.transactionService
                    .getTransactionsByTypeAndCategory(user, category, Type.Expense);
            transactionsByTypeAndCategory = this.transactionService
                    .getTransactionsByMonthAndYear(transactionsByTypeAndCategory, month, year);

            Double sum = sum(transactionsByTypeAndCategory);
            Double res = sum * 100 / total;

            result.add(res);
        }

        return result;
    }

    @Override
    public List<Double> getStatisticsForIncomesByCategory(User user, StatisticsDto statisticsDto) {
        List<Double> result = new ArrayList<>();
        List<Category> categories = this.categoryService.getAll();
        String month = statisticsDto.getMonth();
        int year = statisticsDto.getYear();

        List<Transaction> transactions = this.transactionService
                .getTransactionsByMonthAndYearAndType(user, month, year, Type.Income);
        Double total = sum(transactions);

        for (Category category : categories) {
            List<Transaction> transactionsByTypeAndCategory = this.transactionService
                    .getTransactionsByTypeAndCategory(user, category, Type.Income);
            transactionsByTypeAndCategory = this.transactionService
                    .getTransactionsByMonthAndYear(transactionsByTypeAndCategory, month, year);

            Double sum = sum(transactionsByTypeAndCategory);
            Double res = sum * 100 / total;

            result.add(res);
        }

        return result;
    }

    @Override
    public List<StatisticsDto> getStatisticsByYear(User user, int year) {
        List<StatisticsDto> listWithStatistics = new ArrayList<>();

        for (Month month : Month.values()) {
            List<Transaction> transactions = this.transactionService
                    .getTransactionsByMonthAndYearAndType(user, month.toString(), year, Type.Expense);
            int numberOfExpenses = transactions.size();
            Double expenseSum = sum(transactions);

            transactions = this.transactionService
                    .getTransactionsByMonthAndYearAndType(user, month.toString(), year, Type.Income);
            int numberOfIncomes = transactions.size();
            Double incomeSum = sum(transactions);

            StatisticsDto statisticsDto = StatisticsDto.builder()
                    .month(month.toString())
                    .username(user.getUsername())
                    .userId(user.getId())
                    .year(year)
                    .expense(expenseSum)
                    .income(incomeSum)
                    .numberOfExpenses(numberOfExpenses)
                    .numberOfIncomes(numberOfIncomes)
                    .build();

            listWithStatistics.add(statisticsDto);
        }

        return listWithStatistics;
    }

    @Override
    public StatisticsDto sumTotal(User user, int year) {
        List<Transaction> transactions = this.transactionService.getTransactionsByType(user, Type.Expense);
        transactions = this.transactionService.getTransactionsByYear(transactions, year);
        Double expenseTotal = sum(transactions);

        transactions = this.transactionService.getTransactionsByType(user, Type.Income);
        transactions = this.transactionService.getTransactionsByYear(transactions, year);
        Double incomeTotal = sum(transactions);

        StatisticsDto statisticsDto = StatisticsDto.builder()
                .year(year)
                .username(user.getUsername())
                .sumOfIncomes(expenseTotal)
                .sumOfExpenses(incomeTotal)
                .build();

        return statisticsDto;
    }

    private Double sum(List<Transaction> transactions) {
        return transactions.stream().map(Transaction::getAmount).reduce((double) 0, Double::sum);
    }
}