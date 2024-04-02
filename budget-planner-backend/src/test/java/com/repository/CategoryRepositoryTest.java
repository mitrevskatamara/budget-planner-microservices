package com.repository;

import com.model.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    public void setup() {
        category = Category.builder().name("category").build();
        categoryRepository.save(category);
    }

    @Test
    public void saveCategoryTest() {
        // given

        // when
        Category savedCategory = categoryRepository.save(category);

        // then
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo(category.getName());
    }

    @Test
    public void getAllCategoriesTest() {
        // given
        Category category1 = Category.builder().name("category1").build();
        Category category2 = Category.builder().name("category2").build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        // when
        List<Category> categories = categoryRepository.findAll();

        // then
        assertThat(categories).isNotNull();
    }

    @Test
    public void getCategoryByIdTest() {
        // given

        // when
        Optional<Category> savedCategory = categoryRepository.findById(category.getId());

        // then
        assertThat(savedCategory).isNotNull();
    }

    @Test
    public void updateCategoryTest() {
        // given

        // when
        Category savedCategory = categoryRepository.findById(category.getId()).get();
        savedCategory.setName("category1");
        Category updatedCategory = categoryRepository.save(savedCategory);

        // then
        assertThat(updatedCategory).isNotNull();
        assertThat(updatedCategory.getName()).isEqualTo("category1");
    }

    @Test
    public void deleteCategoryTest() {
        // given

        // when
        categoryRepository.delete(category);
        Optional<Category> optionalCategory = categoryRepository.findById(category.getId());

        // then
        assertThat(optionalCategory).isEmpty();
    }
}