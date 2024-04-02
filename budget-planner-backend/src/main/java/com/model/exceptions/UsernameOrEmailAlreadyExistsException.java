package com.model.exceptions;

public class UsernameOrEmailAlreadyExistsException extends RuntimeException{

    public UsernameOrEmailAlreadyExistsException(String usernameOrEmail) {
        super(String.format("User with: %s already exists!", usernameOrEmail));
    }
}