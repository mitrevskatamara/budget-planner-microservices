package com.service;

import com.model.Budget;
import com.model.Transaction;
import com.model.User;
import com.model.dto.BudgetDto;
import com.repository.BudgetRepository;
import com.service.impl.BudgetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {

    private static final LocalDate DATE = LocalDate.now();
    private static final double AMOUNT = 0.0;

    @InjectMocks
    private BudgetServiceImpl budgetService;

    @Mock
    private BudgetRepository budgetRepository;

    private Budget budget;

    private User user;

    private Transaction transaction;

    private BudgetDto budgetDto;

    @BeforeEach
    public void setup() {
        user = User.builder().build();
        budget = Budget.builder()
                .user(user).month(Month.JANUARY).build();
        transaction = Transaction.builder()
                .amount(AMOUNT).user(user).date(DATE).build();
        budgetDto = new BudgetDto();
        budgetDto.setMonth("JANUARY");
    }

    @Test
    public void createBudgetTest() {
        // given
        when(budgetRepository.save(budget)).thenReturn(budget);

        // when
        BudgetDto savedBudget = budgetService.create(user, budgetDto);

        // then
        verify(budgetRepository).save(budget);
        assertThat(savedBudget).isNotNull();
    }

    @Test
    public void updateAfterAddBudgetTest() {
//        // given
//        when(budgetRepository.findByUserAndMonthAndYear(user, DATE.getMonth(), DATE.getYear()))
//                .thenReturn(budget);
//        when(budgetRepository.save(budget)).thenReturn(budget);
//
//        // when
//        Budget savedBudget = savingService.updateBudgetAfterAddTransaction(transaction);
//
//        // then
//        verify(budgetRepository).findByUserAndMonthAndYear(user, DATE.getMonth(), DATE.getYear());
//        verify(budgetRepository).save(budget);
//        assertThat(savedBudget).isNotNull();
//        assertThat(savedBudget).isEqualTo(budget);
    }

    @Test
    public void updateAfterAddBudgetIfSavingNotExistsTest() {
//        // given
//        when(budgetRepository.findByUserAndMonthAndYear(user, DATE.getMonth(), DATE.getYear()))
//                .thenReturn(null);
//        when(savingService.create(user, budgetDto)).thenReturn(budget);
//
//        // when
//        Budget savedBudget = savingService.updateBudgetAfterAddTransaction(transaction);
//
//        // then
//        verify(budgetRepository).findByUserAndMonthAndYear(user, DATE.getMonth(), DATE.getYear());
//        assertThat(savedBudget).isNotNull();
//        assertThat(savedBudget).isEqualTo(budget);
    }

    @Test
    public void updateAfterDeleteBudgetTest() {
//        // given
//        when(budgetRepository.findByUserAndMonthAndYear(user, DATE.getMonth(), DATE.getYear()))
//                .thenReturn(budget);
//        when(budgetRepository.save(budget)).thenReturn(budget);
//
//        // when
//        Budget savedBudget = savingService.updateBudgetAfterDeleteTransaction(transaction);
//
//        // then
//        verify(budgetRepository).findByUserAndMonthAndYear(user, DATE.getMonth(), DATE.getYear());
//        verify(budgetRepository).save(budget);
//        assertThat(savedBudget).isNotNull();
//        assertThat(savedBudget).isEqualTo(budget);
    }
    @Test
    public void updateAfterUpdateBudgetTest() {
//        // given
//        TransactionDto transactionDto = TransactionDto.builder().type(Type.EXPENSE).amount(AMOUNT).build();
//        when(budgetRepository.findByUserAndMonthAndYear(user, DATE.getMonth(), DATE.getYear()))
//                .thenReturn(budget);
//        when(budgetRepository.save(budget)).thenReturn(budget);
//
//        // when
//        Budget savedBudget = savingService.updateBudgetAfterUpdateTransaction(transaction, transactionDto);
//
//        // then
//        verify(budgetRepository).findByUserAndMonthAndYear(user, DATE.getMonth(), DATE.getYear());
//        verify(budgetRepository).save(budget);
//        assertThat(savedBudget).isNotNull();
//        assertThat(savedBudget).isEqualTo(budget);
    }

    @Test
    public void findSavingByUserAndMonthAndYearTest() {
//        // given
//        when(budgetRepository.findByUserAndMonthAndYear(user, DATE.getMonth(), DATE.getYear()))
//                .thenReturn(budget);
//
//        // when
//        Budget foundBudget = savingService.findByUserAndMonthAndYear(user, DATE.getMonth(), DATE.getYear());
//
//        // then
//        verify(budgetRepository).findByUserAndMonthAndYear(user, DATE.getMonth(), DATE.getYear());
//        assertThat(foundBudget).isNotNull();
//        assertThat(foundBudget).isEqualTo(budget);
    }

    @Test
    public void findByUserTest() {
        // given
        List<Budget> budgets = new ArrayList<>();
        when(budgetRepository.findByUser(user)).thenReturn(budgets);

        // when
        List<BudgetDto> budgetList = budgetService.findByUser(user);

        // then
        verify(budgetRepository).findByUser(user);
        assertThat(budgetList).isNotNull();
    }
}