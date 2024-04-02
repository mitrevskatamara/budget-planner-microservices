package com.service.impl;

import com.mapper.BudgetMapper;
import com.model.Budget;
import com.model.Transaction;
import com.model.User;
import com.model.dto.BudgetDto;
import com.model.dto.FilterDto;
import com.model.dto.TransactionDto;
import com.model.enumerations.Type;
import com.model.exceptions.BudgetAlreadyExistsException;
import com.model.exceptions.ResourceNotFoundException;
import com.repository.BudgetRepository;
import com.service.BudgetService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;

    @Override
    public List<BudgetDto> getAll() {
        return this.budgetRepository.findAll()
                .stream().map(budget -> BudgetMapper.mapToBudgetDto(new BudgetDto(), budget)).collect(Collectors.toList());
    }

    @Override
    public Budget getBudgetById(Long id) {
        return this.budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget", "id", id.toString()));
    }

    @Override
    public BudgetDto create(User user, BudgetDto budgetDto) {
        if (budgetRepository.existsByMonthAndYearAndUser(Month.valueOf(budgetDto.getMonth()), budgetDto.getYear(), user)) {
            throw new BudgetAlreadyExistsException();
        }

        Budget budget = BudgetMapper.mapToBudget(budgetDto, new Budget(), user);
        Budget savedBudget = this.budgetRepository.save(budget);
        return BudgetMapper.mapToBudgetDto(new BudgetDto(), savedBudget);
    }

    @Override
    public BudgetDto update(Long id, BudgetDto budgetDto) {
        Budget budget = this.getBudgetById(id);
        Double budgetValue = budgetDto.getBudget();
        Double currentBalance = budget.getBalance();
        double difference;

        if (budget.getBudget() < budgetValue) {
            difference = budgetValue - budget.getBudget();
            currentBalance += difference;
        } else {
            difference = budget.getBudget() - budgetValue;
            currentBalance -= difference;
        }

        budget.setBalance(currentBalance);
        budget.setBudget(budgetValue);

        Budget updatedBudget = this.budgetRepository.save(budget);

        return BudgetMapper.mapToBudgetDto(new BudgetDto(), updatedBudget);
    }

    @Override
    public Budget updateBudgetAfterAddTransaction(Transaction transaction) {
        Budget budget = this.findByUserAndMonthAndYear
                (transaction.getUser(), transaction.getDate().getMonth(), transaction.getDate().getYear());

        Double balance = budget.getBalance();

        if (transaction.getTransactionType().getType().equals(Type.Expense)) {
            balance -= transaction.getAmount();
        } else {
            balance += transaction.getAmount();
        }

        budget.setBalance(balance);

        return this.budgetRepository.save(budget);
    }

    @Override
    public Budget updateBudgetAfterDeleteTransaction(Transaction transaction) {
        Budget budget = this.findByUserAndMonthAndYear
                (transaction.getUser(), transaction.getDate().getMonth(), transaction.getDate().getYear());

        Double balance = budget.getBalance();

        if (transaction.getTransactionType().getType().equals(Type.Expense)) {
            balance += transaction.getAmount();
        } else {
            balance -= transaction.getAmount();
        }

        budget.setBalance(balance);

        return this.budgetRepository.save(budget);
    }

    @Override
    public Budget updateBudgetAfterUpdateTransaction(Transaction transaction, TransactionDto transactionDto) {
        Budget budget = this.findByUserAndMonthAndYear
                (transaction.getUser(), transaction.getDate().getMonth(), transaction.getDate().getYear());

        Double balance = budget.getBalance();

        Double oldBalance = transaction.getAmount();
        Double newBalance = transactionDto.getAmount();

        Type oldType = transaction.getTransactionType().getType();
        Type newType = transactionDto.getType();

        if (oldType.equals(Type.Expense) && newType.equals(Type.Expense)) {
            balance += oldBalance;
            balance -= newBalance;
        } else if (oldType.equals(Type.Income) && newType.equals(Type.Income)) {
            balance -= oldBalance;
            balance += newBalance;
        } else if (oldType.equals(Type.Expense) && newType.equals(Type.Income)) {
            balance += oldBalance;
            balance += newBalance;
        } else if (oldType.equals(Type.Income) && newType.equals(Type.Expense)){
            balance -= oldBalance;
            balance -= newBalance;
        }

        budget.setBalance(balance);

        return this.budgetRepository.save(budget);
    }

    @Override
    public Budget findByUserAndMonthAndYear(User user, Month month, int year) {
        return this.budgetRepository.findByMonthAndYearAndUser(month, year, user);
    }

    @Override
    public List<BudgetDto> findByUser(User user) {
        return this.budgetRepository.findByUser(user)
                .stream()
                .sorted(Comparator.comparingInt(Budget::getYear)
                        .thenComparing(Budget::getMonth, Comparator.reverseOrder()))
                .map(budget -> BudgetMapper.mapToBudgetDto(new BudgetDto(), budget))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean existsByMonthAndYear(Month month, int year, User user) {
        return this.budgetRepository.existsByMonthAndYearAndUser(month, year, user);
    }

    @Override
    public List<BudgetDto> filterBudgets(User user, FilterDto filterDto) {
        List<Budget> budgets;

        if (!filterDto.getMonth().isEmpty() && filterDto.getYear() == 0) {
            budgets = this.budgetRepository.findByUserAndMonth(user, Month.valueOf(filterDto.getMonth()));
        } else if (filterDto.getMonth().isEmpty() && filterDto.getYear() != 0) {
            budgets = this.budgetRepository.findByUserAndYear(user, filterDto.getYear());
        } else if (filterDto.getMonth().isEmpty() && filterDto.getYear() == 0) {
            budgets = this.budgetRepository.findByUser(user);
        } else{
            budgets = this.budgetRepository.findByUserAndMonthAndYear
                    (user, Month.valueOf(filterDto.getMonth()), filterDto.getYear());
        }

        return budgets.stream().map(budget -> BudgetMapper.mapToBudgetDto(new BudgetDto(), budget)).collect(Collectors.toList());
    }
}
