package com.example.lgimtest.errorhandling;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Error response for the service.
 */
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String message;

    public ErrorResponse(Throwable e) {
        this.timestamp = LocalDateTime.now();

        if (e.getMessage() == null || e.getMessage().isEmpty()) {
            this.message = e.getClass().toString();
        }
        else {
            this.message = e.getMessage();
        }
    }
}
