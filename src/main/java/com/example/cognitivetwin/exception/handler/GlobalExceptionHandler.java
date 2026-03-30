package com.example.cognitivetwin.exception.handler;

import com.example.cognitivetwin.exception.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request){
        return ResponseEntity.status(500).body(ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .code(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                .build());
    }

}
