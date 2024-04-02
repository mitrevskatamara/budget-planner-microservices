package com.controller;

import com.model.Category;
import com.model.dto.CategoryDto;
import com.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    public void getAllCategoriesTest() {
        // given
        List<Category> categoryList = new ArrayList<>();
        when(categoryService.getAll()).thenReturn(categoryList);

        // when
        ResponseEntity<List<Category>> response = categoryController.getAllCategories();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getCategoryTest() {
        // given
        Category category = Category.builder().id(1L).build();
        when(categoryService.getCategoryById(1L)).thenReturn(category);

        // when
        ResponseEntity<Category> response = categoryController.getCategory(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(category.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void createCategoryTest() {
        // given
        Category category = Category.builder().build();
        CategoryDto categoryDto = new CategoryDto();
        when(categoryService.create(categoryDto.getName())).thenReturn(category);

        // when
        ResponseEntity<Category> response = categoryController.createCategory(categoryDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void updateCategoryTest() {
        // given
        Category category = Category.builder().build();
        CategoryDto categoryDto = new CategoryDto();
        when(categoryService.update(1L, categoryDto.getName())).thenReturn(category);

        // when
        ResponseEntity<Category> response = categoryController.updateCategory(1L, categoryDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void deleteCategoryTest() {
        // given
        Category category = Category.builder().build();
        when(categoryService.delete(1L)).thenReturn(category);

        // when
        ResponseEntity<Long> response = categoryController.deleteCategory(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}