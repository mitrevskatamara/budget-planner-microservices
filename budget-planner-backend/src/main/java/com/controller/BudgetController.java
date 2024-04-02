package com.controller;

import com.model.Budget;
import com.model.User;
import com.model.dto.BudgetDto;
import com.model.dto.FilterDto;
import com.service.BudgetService;
import com.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/budget")
@Tag(name = "Budget Controller", description = "Endpoints for handling requests for Budget")
public class BudgetController {

    private final BudgetService budgetService;
    private final UserService userService;

    @Operation(summary = "Endpoint to get all created Budgets")
    @GetMapping
    public ResponseEntity<List<BudgetDto>> getAllBudgets() {
        List<BudgetDto> budgets = this.budgetService.getAll();

        return new ResponseEntity<>(budgets, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to get Budget by id")
    @GetMapping("/{id}")
    public ResponseEntity<Budget> getBudget(@PathVariable Long id) {
        Budget budget = this.budgetService.getBudgetById(id);

        return new ResponseEntity<>(budget, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to create new Budget")
    @PostMapping("/create")
    public ResponseEntity<BudgetDto> createBudget(@RequestBody BudgetDto budgetDto) {
        User user = this.userService.findByUsername(budgetDto.getUsername());
        BudgetDto budget =  this.budgetService.create(user, budgetDto);

        return new ResponseEntity<>(budget, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to update existing Budget")
    @PostMapping("/update/{id}")
    public ResponseEntity<BudgetDto> updateBudget(@PathVariable Long id, @RequestBody BudgetDto budgetDto) {
        BudgetDto budget = this.budgetService.update(id, budgetDto);

        return new ResponseEntity<>(budget, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to get filtered Budgets")
    @PostMapping("/filter")
    public ResponseEntity<List<BudgetDto>> filterBudgets(@RequestBody FilterDto filterDto) {
        User user = this.userService.findByUsername(filterDto.getUsername());
        List<BudgetDto> budgets = this.budgetService.filterBudgets(user, filterDto);

        return new ResponseEntity<>(budgets, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to get Budgets by specific user")
    @PostMapping("/filterByUser")
    public ResponseEntity<List<BudgetDto>> filterByUser(@RequestBody FilterDto filterDto) {
        User user = this.userService.findByUsername(filterDto.getUsername());
        List<BudgetDto> budgets = this.budgetService.findByUser(user);

        return new ResponseEntity<>(budgets, HttpStatus.OK);
    }
}
