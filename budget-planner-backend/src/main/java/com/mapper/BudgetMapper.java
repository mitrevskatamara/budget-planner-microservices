package com.mapper;

import com.model.Budget;
import com.model.User;
import com.model.dto.BudgetDto;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class BudgetMapper {

    public static Budget mapToBudget(BudgetDto budgetDto, Budget budget, User user) {
        budget.setUser(user);
        budget.setBudget(budgetDto.getBudget());
        budget.setBalance(budgetDto.getBudget());
        budget.setMonth(Month.valueOf(budgetDto.getMonth()));
        budget.setYear(budgetDto.getYear());
        budget.setCurrency(budgetDto.getCurrency());

        return budget;
    }

    public static BudgetDto mapToBudgetDto(BudgetDto budgetDto, Budget budget) {
        budgetDto.setId(budget.getId());
        budgetDto.setUsername(budget.getUser().getUsername());
        budgetDto.setBudget(budget.getBudget());
        budgetDto.setBalance(budget.getBudget());
        budgetDto.setMonth(budget.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        budgetDto.setYear(budget.getYear());
        budgetDto.setCurrency(budget.getCurrency());

        return budgetDto;
    }
}
