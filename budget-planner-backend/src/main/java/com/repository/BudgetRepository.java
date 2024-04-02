package com.repository;

import com.model.Budget;
import com.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Month;
import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Budget findByMonthAndYearAndUser(Month month, int year, User user);

    List<Budget> findByUserAndMonthAndYear(User user, Month month, int year);

    List<Budget> findByUser(User user);

    List<Budget> findByUserAndYear(User user, int year);

    List<Budget> findByUserAndMonth(User user, Month month);

    Boolean existsByMonthAndYearAndUser(Month month, int year, User user);
}