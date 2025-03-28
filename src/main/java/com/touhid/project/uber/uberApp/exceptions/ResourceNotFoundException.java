package com.touhid.project.uber.uberApp.exceptions;

/**
 * Exception thrown when a requested resource is not found.
 * Example: A user tries to fetch a booking with a non-existent ID.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Default constructor with no message.
     */
    public ResourceNotFoundException() {
        super("The requested resource was not found.");
    }

    /**
     * Constructor with a custom error message.
     *
     * @param message A specific error message.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
