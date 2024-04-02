package com.service;

import com.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAll();

    Category getCategoryById(Long id);

    Category create(String name);

    Category update(Long id, String name);

    Category delete(Long id);
}
