package com.service.impl;

import com.model.Category;
import com.model.exceptions.ResourceNotFoundException;
import com.repository.CategoryRepository;
import com.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAll() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return this.categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category", "id", id.toString()));
    }

    @Override
    public Category create(String name) {
        Category category = Category.builder()
                .name(name)
                .build();

        return this.categoryRepository.save(category);
    }

    @Override
    public Category update(Long id, String name) {
        Category category = this.getCategoryById(id);

        if (Objects.nonNull(name)) {
            category.setName(name);
            category = this.categoryRepository.save(category);
        }

        return category;
    }

    @Override
    public Category delete(Long id) {
        Category category = this.getCategoryById(id);

        this.categoryRepository.delete(category);

        return category;
    }
}