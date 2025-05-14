package com.skillbox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatus status = (HttpStatus) ex.getStatusCode();
        return new ResponseEntity<>(ex.getReason(), status);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleRuntimeException(BusinessException ex) {
        Map<String, ?> body = Map.of(
                "timestamp", DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now()),
                "message", ex.getMessage()
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
