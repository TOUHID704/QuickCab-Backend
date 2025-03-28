package com.touhid.project.uber.uberApp.advices;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Represents a standard API response.
 * Can be used for both successful responses and error responses.
 *
 * @param <T> The type of the data returned in case of a successful response.
 */

@Data
public class ApiResponse<T> {

    /**
     * The timestamp when the response is generated.
     */
    private LocalDateTime timestamp;

    /**
     * The actual data of the response (e.g., user details or booking details).
     * This will be null if the response contains an error.
     */
    private T data;

    /**
     * The error details in case the response represents an error.
     * This will be null if the response is successful.
     */
    private ApiError error;

    /**
     * Default constructor that sets the current timestamp.
     */
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor for successful responses with data.
     *
     * @param data The data to include in the response.
     */
    public ApiResponse(T data) {
        this();
        this.data = data;
    }

    /**
     * Constructor for error responses with error details.
     *
     * @param error The error details to include in the response.
     */
    public ApiResponse(ApiError error) {
        this();
        this.error = error;
    }
}
