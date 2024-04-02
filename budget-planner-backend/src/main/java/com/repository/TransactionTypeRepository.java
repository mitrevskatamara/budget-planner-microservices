package com.repository;

import com.model.Category;
import com.model.TransactionType;
import com.model.enumerations.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long> {

    TransactionType findByCategoryAndType(Category category, Type type);
}