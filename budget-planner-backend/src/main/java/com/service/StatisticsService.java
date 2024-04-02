package com.service;

import com.model.User;
import com.model.dto.StatisticsDto;

import java.util.List;

public interface StatisticsService {

    List<Double> getStatisticsForExpensesByCategory(User user, StatisticsDto statisticsDto);

    List<Double> getStatisticsForIncomesByCategory(User user, StatisticsDto statisticsDto);

    List<StatisticsDto> getNumberAndSumOfTransactionsByMonthAndYear(User user, int year);

    StatisticsDto getTotalSumOfTransactionsByMonthAndYear(User user, int year);
}