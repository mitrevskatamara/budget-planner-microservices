package com.service.impl;

import com.model.Category;
import com.model.TransactionType;
import com.model.dto.TransactionTypeDto;
import com.model.enumerations.Type;
import com.model.exceptions.ResourceNotFoundException;
import com.repository.TransactionTypeRepository;
import com.service.CategoryService;
import com.service.TransactionTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class TransactionTypeServiceImpl implements TransactionTypeService {

    private final TransactionTypeRepository transactionTypeRepository;

    private final CategoryService categoryService;

    @Override
    public TransactionType getTransactionTypeById(Long id) {
        return this.transactionTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction type", "id", id.toString()));
    }

    @Override
    public TransactionType create(TransactionTypeDto transactionTypeDto) {
        Category category = this.categoryService.getCategoryById(transactionTypeDto.getCategoryId());
        TransactionType transactionType = TransactionType.builder()
                .type(transactionTypeDto.getType())
                .category(category)
                .build();

        return this.transactionTypeRepository.save(transactionType);
    }

    @Override
    public TransactionType update(Long id, TransactionTypeDto transactionTypeDto) {
        return null;
    }

    @Override
    public TransactionType delete(Long id) {
        TransactionType transactionType = this.getTransactionTypeById(id);

        this.transactionTypeRepository.delete(transactionType);

        return transactionType;
    }

    @Override
    public TransactionType findByCategoryAndType(Long categoryId, Type type) {
        Category category = this.categoryService.getCategoryById(categoryId);
        TransactionType transactionType = this.transactionTypeRepository.findByCategoryAndType(category, type);

        if (Objects.isNull(transactionType)) {
            TransactionTypeDto transactionTypeDto = new TransactionTypeDto(type, categoryId);
            transactionType = this.create(transactionTypeDto);
        }

        return transactionType;
    }
}