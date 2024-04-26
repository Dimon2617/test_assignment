package org.example.test_assignment.common;

import org.example.test_assignment.user.exceptions.BaseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RestExceptionHandlerTest {

    @Autowired
    private RestExceptionHandler restExceptionHandler;

    @Test
    public void testRestExceptionHandler() {
        BaseException baseException = new BaseException("Test message", "Not Found", 404);
        ResponseEntity<ErrorResponse> responseEntity = ResponseEntity
                .status(baseException.getCode())
                .body(ErrorResponse.builder()
                        .message(baseException.getMessage())
                        .status(baseException.getCode())
                        .error(baseException.getError())
                        .build());

        restExceptionHandler.handleBaseException(baseException);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Test message", responseEntity.getBody().getMessage());
        assertEquals("Not Found", responseEntity.getBody().getError());
    }
}
