package com.controller;

import com.model.Transaction;
import com.model.User;
import com.model.dto.TransactionDto;
import com.model.dto.FilterDto;
import com.service.TransactionService;
import com.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/transactions")
@Tag(name = "Transaction Controller", description = "Endpoints for handling requests for Transactions")
public class TransactionController {

    private final TransactionService transactionService;

    private final UserService userService;

    @Operation(summary = "Endpoint to get all created Transactions")
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return new ResponseEntity<>(this.transactionService.getAll(), HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to get Transaction by id")
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) {
        Transaction transaction = this.transactionService.getTransactionById(id);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to add new Transaction")
    @PostMapping("/add")
    public ResponseEntity<Transaction> addTransaction(@RequestBody TransactionDto transactionDto) {
        User user = this.userService.findByUsername(transactionDto.getUsername());
        Transaction transaction = this.transactionService.addTransaction(transactionDto, user);

        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to update existing Transaction")
    @PostMapping("/update/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id,
                                                         @RequestBody TransactionDto transactionDto) {
        Transaction transaction = this.transactionService.update(id, transactionDto);

        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to delete existing Transaction")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Long> deleteTransaction(@PathVariable Long id) {
        Long deletedId = this.transactionService.delete(id);
        return new ResponseEntity<>(deletedId, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to get filtered Transactions")
    @PostMapping("/filter")
    public ResponseEntity<List<Transaction>> filterTransactions(@RequestBody FilterDto filterDto) {
        User user = this.userService.findByUsername(filterDto.getUsername());
        List<Transaction> transactions = this.transactionService.filterTransactions(user, filterDto);

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to get Transactions by specific user")
    @PostMapping("/filterByUser")
    public ResponseEntity<List<Transaction>> filterByUser(@RequestBody FilterDto filterDto) {
        User user = this.userService.findByUsername(filterDto.getUsername());
        List<Transaction> transactions = this.transactionService.getTransactionsByUser(user);

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to get Transactions by specific date")
    @PostMapping("/filterByDate")
    public ResponseEntity<List<Transaction>> filterByDate(@RequestBody FilterDto filterDto) {
        User user = this.userService.findByUsername(filterDto.getUsername());
        List<Transaction> transactions = this.transactionService.getTransactionsByDate(user, filterDto);

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}