package com.model.exceptions;

public class BudgetAlreadyExistsException extends RuntimeException {

    public BudgetAlreadyExistsException() {
        super("Budget already exists!");
    }
}