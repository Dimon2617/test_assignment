package org.example.test_assignment.user.exceptions;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final String error;
    private final int code;

    public BaseException(String message, String error, int code) {
        super(message);
        this.error = error;
        this.code = code;
    }

}
