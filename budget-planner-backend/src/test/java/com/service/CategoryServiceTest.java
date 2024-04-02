package com.service;

import com.model.Category;
import com.model.exceptions.ResourceNotFoundException;
import com.repository.CategoryRepository;
import com.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    private static final long ID = 1L;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    public void setUp() {
        category = Category.builder().name("category").build();
    }

    @Test
    public void getAllCategoriesTest() {
        // given
        List<Category> categoryList = new ArrayList<>();
        when(categoryRepository.findAll()).thenReturn(categoryList);

        // when
        List<Category> list = categoryService.getAll();

        // then
        verify(categoryRepository).findAll();
        assertThat(list).isNotNull();
    }

    @Test
    public void getCategoryByIdTest() {
        // given
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        // when
        Category categoryFromDb = categoryService.getCategoryById(category.getId());

        // then
        verify(categoryRepository).findById(any());
        assertThat(categoryFromDb).isNotNull();
    }
    @Test
    public void getCategoryByIdUnsuccessfullyTest() {
        // given
        when(categoryRepository.findById(ID))
                .thenThrow(new ResourceNotFoundException("Category", "id", String.valueOf(ID)));

        // when

        // then
        assertThatThrownBy(() -> categoryService.getCategoryById(ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .message().isEqualTo("Category not found with id : '1'");
    }

    @Test
    public void createCategoryTest() {
        // given
        when(categoryRepository.save(category)).thenReturn(category);

        // when
        Category savedCategory = categoryService.create("category");

        // then
        verify(categoryRepository).save(savedCategory);
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("category");
    }

    @Test
    public void updateCategorySuccessfullyTest() {
        // given
        when(categoryRepository.findById(ID)).thenReturn(Optional.of(category));
        category.setName("category1");
        when(categoryRepository.save(category)).thenReturn(category);

        // when
        Category updatedCategory = categoryService.update(ID, "category1");

        // then
        verify(categoryRepository).findById(ID);
        verify(categoryRepository).save(updatedCategory);
        assertThat(updatedCategory).isNotNull();
    }

    @Test
    public void updateCategoryUnsuccessfullyTest() {
        // given
        when(categoryRepository.findById(ID))
                .thenThrow(new ResourceNotFoundException("Category", "id", String.valueOf(ID)));

        // when

        // then
        assertThatThrownBy(() -> categoryService.update(ID,""))
                .isInstanceOf(ResourceNotFoundException.class)
                .message().isEqualTo("Category not found with id : '1'");;
    }

    @Test
    public void deleteCategoryTest() {
        // given
        Category cat = Category.builder().id(ID).build();
        when(categoryRepository.findById(ID)).thenReturn(Optional.of(cat));
        willDoNothing().given(categoryRepository).delete(cat);

        // when
        Category c = categoryService.delete(ID);

        // then
        verify(categoryRepository).delete(cat);
        verify(categoryRepository).findById(ID);
        assertThat(c).isNotNull();
    }

    @Test
    public void deleteCategoryThrowExceptionTest() {
        // given
        when(categoryRepository.findById(ID))
                .thenThrow(new ResourceNotFoundException("Category", "id", String.valueOf(ID)));

        // when

        // then
        assertThatThrownBy(() -> categoryService.getCategoryById(ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .message().isEqualTo("Category not found with id : '1'");
    }
}