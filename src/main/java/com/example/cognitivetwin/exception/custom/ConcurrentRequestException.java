package com.example.cognitivetwin.exception.custom;

import org.springframework.web.bind.annotation.ResponseStatus;


public class ConcurrentRequestException extends RuntimeException {
    public ConcurrentRequestException(String message) {
        super(message);
    }
}
