package org.example.test_assignment.user.exceptions;

public class InvalidDateRangeException extends BaseException {

    public InvalidDateRangeException(String message) {
        super(message, "Conflict", 409);
    }
}
