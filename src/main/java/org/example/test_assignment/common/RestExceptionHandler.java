package org.example.test_assignment.common;

import org.example.test_assignment.user.exceptions.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
        ErrorResponse body = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(ex.getCode())
                .error(ex.getError())
                .build();

        return new ResponseEntity<>(body, HttpStatus.valueOf(ex.getCode()));
    }

}
