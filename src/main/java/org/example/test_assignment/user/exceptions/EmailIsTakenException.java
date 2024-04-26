package org.example.test_assignment.user.exceptions;

public class EmailIsTakenException extends BaseException {

    public EmailIsTakenException(String message) {
        super(message, "Conflict", 409);
    }

}
