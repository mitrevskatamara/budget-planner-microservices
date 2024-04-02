package com.controller;

import com.model.Budget;
import com.model.User;
import com.model.dto.BudgetDto;
import com.model.dto.FilterDto;
import com.service.BudgetService;
import com.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BudgetControllerTest {

    @Mock
    private BudgetService budgetService;

    @Mock
    private UserService userService;

    private Budget budget;

    private BudgetDto budgetDto;

    private User user;

    private List<Budget> budgets;

    private List<BudgetDto> budgetDtoList;

    private FilterDto filterDto;

    @InjectMocks
    private BudgetController budgetController;

    @BeforeEach
    public void setup() {
        budget = Budget.builder().id(1L).build();
        budgetDto = new BudgetDto();
        user = User.builder().build();
        budgets = new ArrayList<>();
        budgetDtoList = new ArrayList<>();
        filterDto = FilterDto.builder().build();
    }

    @Test
    public void getAllBudgetsTest() {
        // given
        when(budgetService.getAll()).thenReturn(budgetDtoList);

        // when
        ResponseEntity<List<BudgetDto>> response = budgetController.getAllBudgets();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getBudgetTest() {
        // given
        when(budgetService.getBudgetById(1L)).thenReturn(budget);

        // when
        ResponseEntity<Budget> response = budgetController.getBudget(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void createBudgetTest() {
        // given
        when(userService.findByUsername(budgetDto.getUsername())).thenReturn(user);
        when(budgetService.create(user, budgetDto)).thenReturn(budgetDto);

        // when
        ResponseEntity<BudgetDto> response = budgetController.createBudget(budgetDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void updateBudgetTest() {
        // given
        when(budgetService.update(1L, budgetDto)).thenReturn(budgetDto);

        // when
        ResponseEntity<BudgetDto> response = budgetController.updateBudget(1L, budgetDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void filterBudgetsTest() {
        // when
        when(userService.findByUsername(budgetDto.getUsername())).thenReturn(user);
        when(budgetService.filterBudgets(user, filterDto)).thenReturn(budgetDtoList);

        // when
        ResponseEntity<List<BudgetDto>> response = budgetController.filterBudgets(filterDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    public void filterByUserTest() {
        // given
        when(userService.findByUsername(filterDto.getUsername())).thenReturn(user);
        when(budgetService.findByUser(user)).thenReturn(budgetDtoList);

        // when
        ResponseEntity<List<BudgetDto>> response = budgetController.filterByUser(filterDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
