package com.model.exceptions;

public class PasswordsDoNotMatchException extends RuntimeException {

    public PasswordsDoNotMatchException() {
        super("Passwords do not match. Please write the correct password.");
    }
}
