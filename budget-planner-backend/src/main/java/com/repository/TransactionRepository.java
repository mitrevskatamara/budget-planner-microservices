package com.repository;

import com.model.Category;
import com.model.Transaction;
import com.model.User;
import com.model.enumerations.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUser(User user);

    List<Transaction> findByUserAndDate(User user, LocalDate localDate);

    List<Transaction> findByUserAndTransactionType_Type(User user, Type type);

    List<Transaction> findByUserAndTransactionType_CategoryAndTransactionType_Type(User user, Category category, Type type);
}