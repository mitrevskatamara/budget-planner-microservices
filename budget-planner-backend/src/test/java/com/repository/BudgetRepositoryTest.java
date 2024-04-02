package com.repository;

import com.model.Budget;
import com.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class BudgetRepositoryTest {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserRepository userRepository;

    private Budget budget;
    private User user;

    @BeforeEach
    public void setup() {
        user = User.builder().build();
        userRepository.save(user);
        budget = Budget.builder()
                .month(LocalDate.now().getMonth())
                .year(LocalDate.now().getYear())
                .user(user).build();
        budgetRepository.save(budget);
    }

    @Test
    public void saveUserTest() {
        // given

        // when
        Budget savedBudget = budgetRepository.save(budget);

        // then
        assertThat(savedBudget).isNotNull();
    }

    @Test
    public void getUserByIdTest() {
        // given

        // when
        Optional<Budget> sav = budgetRepository.findById(budget.getId());

        // then
        assertThat(sav).isNotNull();
    }

    @Test
    public void getAllUsersTest() {
        // given
        Budget budget1 = Budget.builder().build();
        Budget budget2 = Budget.builder().build();

        budgetRepository.save(budget1);
        budgetRepository.save(budget2);

        // when
        List<Budget> budgets = budgetRepository.findAll();

        // then
        assertThat(budgets).isNotNull();
    }

//    @Test
//    public void updateUserTest() {
//        // given
//
//        // when
//        Budget savedBudget = budgetRepository.findById(budget.getId()).get();
//        savedBudget.setTotal(200.0);
//        Budget updatedBudget = budgetRepository.save(budget);
//
//        // then
//        assertThat(updatedBudget).isNotNull();
//        assertThat(updatedBudget.getTotal()).isEqualTo(200.0);
//    }

    @Test
    public void deleteUserTest() {
        // given

        // when
        budgetRepository.delete(budget);
        Optional<Budget> optionalSaving = budgetRepository.findById(budget.getId());

        // then
        assertThat(optionalSaving).isEmpty();
    }

    @Test
    public void findSavingsByUserSuccessfullyTest() {
        // given

        // when
        List<Budget> budgets = budgetRepository.findByUser(user);

        // then
        assertThat(budgets).isNotNull();
    }

    @Test
    public void findSavingsByUserUnsuccessfullyTest() {
        // given
        User newUser = User.builder().build();
        userRepository.save(newUser);

        // when
        List<Budget> budgets = budgetRepository.findByUser(newUser);

        // then
        assertThat(budgets).isEmpty();
    }

    @Test
    public void findSavingByUserAndMonthAndYearSuccessfullyTest() {
//        // given
//
//        // when
//        Budget foundBudget = budgetRepository.findByUserAndMonthAndYear(user, LocalDate.now().getMonth(),
//                LocalDate.now().getYear());
//
//        // then
//        assertThat(foundBudget).isNotNull();
//        assertThat(foundBudget).isEqualTo(budget);
    }

    @Test
    public void findSavingByUserAndMonthAndYearUnsuccessfullyTest() {
//        // given
//
//        // when
//        Budget foundBudget = budgetRepository.findByUserAndMonthAndYear
//                (user, LocalDate.now().getMonth().plus(1L), LocalDate.now().getYear());
//
//        // then
//        assertThat(foundBudget).isEqualTo(null);
    }
}
