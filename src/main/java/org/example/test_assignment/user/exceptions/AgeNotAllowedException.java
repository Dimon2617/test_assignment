package org.example.test_assignment.user.exceptions;

public class AgeNotAllowedException extends BaseException {

    public AgeNotAllowedException(String message) {
        super(message, "Conflict", 409);
    }

}
