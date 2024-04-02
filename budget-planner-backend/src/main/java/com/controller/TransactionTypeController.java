package com.controller;

import com.model.TransactionType;
import com.model.dto.TransactionTypeDto;
import com.service.TransactionTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/transactionTypes")
@Tag(name = "Transaction type Controller", description = "Endpoints for handling requests for Transaction types")
public class TransactionTypeController {

    private final TransactionTypeService transactionTypeService;

    @Operation(summary = "Endpoint to create new transaction type")
    @PostMapping("/create")
    public ResponseEntity<TransactionType> create(@RequestBody TransactionTypeDto transactionTypeDto) {
        TransactionType transactionType = this.transactionTypeService.create(transactionTypeDto);

        return new ResponseEntity<>(transactionType, HttpStatus.OK);
    }
}