package com.touhid.project.uber.uberApp.advices;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Represents details of an API error.
 * This is returned to the client when an error occurs.
 */
@Data
@Builder
public class ApiError {

    /**
     * HTTP status of the error (e.g., 404 for Not Found, 409 for Conflict).
     */
    private HttpStatus httpStatus;

    /**
     * A human-readable message explaining the error.
     */
    private String message;

    /**
     * A list of sub-errors providing more specific details about the error (if applicable).
     */
    private List<String> subErrors;
}
