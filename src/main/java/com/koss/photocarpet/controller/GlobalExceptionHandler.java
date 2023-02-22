package com.koss.photocarpet.controller;

import com.koss.photocarpet.controller.dto.response.ExceptionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArguments(IllegalArgumentException exception){
        return ResponseEntity.badRequest().body(ExceptionDTO.builder().error(exception.getMessage()).build());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(IllegalArgumentException exception){
        return ResponseEntity.badRequest().body(ExceptionDTO.builder().error(exception.getMessage()).build());
    }
}