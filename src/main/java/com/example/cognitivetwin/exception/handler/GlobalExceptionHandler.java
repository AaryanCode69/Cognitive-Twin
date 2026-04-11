package com.example.cognitivetwin.exception.handler;

import com.example.cognitivetwin.exception.custom.ConcurrentRequestException;
import com.example.cognitivetwin.exception.custom.EmailAlreadyExistsException;
import com.example.cognitivetwin.exception.custom.ResourceNotFoundException;
import com.example.cognitivetwin.exception.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex,HttpServletRequest request){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName,errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .path(request.getRequestURI())
                        .timestamp(Instant.now())
                        .message("Validation failed")
                        .validationErrors(errors)
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request){
        log.error("Unhandled exception occurred", ex);
        return ResponseEntity.status(500).body(ErrorResponse.builder()
                        .timestamp(Instant.now())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("Something went wrong. Please try again later.")
                        .path(request.getRequestURI())
                .build());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex ,HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex ,HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build());
    }

    public ResponseEntity<ErrorResponse> handleConcurrentRequestException(ConcurrentRequestException ex,HttpServletRequest request){
        return  ResponseEntity.status(HttpStatus.CONFLICT.value()).body(
         ErrorResponse.builder()
                 .timestamp(Instant.now())
                 .status(HttpStatus.CONFLICT.value())
                 .message(ex.getMessage())
                 .path(request.getRequestURI())
                 .build()
        );
    }

}
