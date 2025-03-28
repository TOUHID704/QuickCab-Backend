package com.touhid.project.uber.uberApp.exceptions;

/**
 * Exception thrown when there is a conflict during processing.
 * Example: Trying to create a booking for a time slot that is already reserved.
 */
public class RuntimeConflictException extends RuntimeException {

    /**
     * Default constructor with no message.
     */
    public RuntimeConflictException() {
        super("There was a conflict during processing.");
    }

    /**
     * Constructor with a custom error message.
     *
     * @param message A specific error message.
     */
    public RuntimeConflictException(String message) {
        super(message);
    }
}
