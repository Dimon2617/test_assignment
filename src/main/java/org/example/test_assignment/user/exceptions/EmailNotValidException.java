package org.example.test_assignment.user.exceptions;

public class EmailNotValidException extends BaseException {

    public EmailNotValidException(String message) {
        super(message, "Conflict", 409);
    }

}
