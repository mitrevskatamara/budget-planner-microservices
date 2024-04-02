package com.controller;

import com.model.Category;
import com.model.dto.CategoryDto;
import com.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/categories")
@CrossOrigin("http://localhost:3000")
@Tag(name = "Category Controller", description = "Endpoints for handling requests for Categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Endpoint to get all Categories")
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = this.categoryService.getAll();

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to get Category by id")
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        Category category = this.categoryService.getCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to create new Category")
    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDto categoryDto) {
        Category category = this.categoryService.create(categoryDto.getName());

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to update existing Category")
    @PostMapping("/update/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        Category category = this.categoryService.update(id, categoryDto.getName());
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to delete existing Category")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Long> deleteCategory(@PathVariable Long id) {
        Category category = this.categoryService.delete(id);
        return new ResponseEntity<>(category.getId(), HttpStatus.OK);
    }
}
