package org.example.test_assignment.user.exceptions;

public class UserNotExistsException extends BaseException {

    public UserNotExistsException(String message) {
        super(message, "Not Found", 404);
    }

}
