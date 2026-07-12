package com.carwashpro.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class ApiError {

    private LocalDateTime timestamp;

    private int status;

    private String error;

    private String message;

    private String path;

    private Map<String, String> validationErrors;

}