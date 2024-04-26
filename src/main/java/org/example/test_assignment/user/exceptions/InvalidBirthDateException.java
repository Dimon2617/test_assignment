package org.example.test_assignment.user.exceptions;

public class InvalidBirthDateException extends BaseException {

    public InvalidBirthDateException(String message) {
        super(message, "Conflict", 409);
    }

}
