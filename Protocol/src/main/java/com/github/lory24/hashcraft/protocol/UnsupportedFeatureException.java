package com.github.lory24.hashcraft.protocol;

/**
 * This exception will be thrown when the client wants to use a feature that hasn't been implemented yet.
 */
public class UnsupportedFeatureException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public UnsupportedFeatureException(String message) {
        super(message);
    }
}
