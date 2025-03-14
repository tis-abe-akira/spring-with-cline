package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * APIキー認証に関連する例外を処理するControllerAdvice
 */
@ControllerAdvice
public class ApiKeyExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException e) {
        ErrorResponse errorResponse = new ErrorResponse(
            e.getStatus().value(),
            e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, e.getStatus());
    }
}
