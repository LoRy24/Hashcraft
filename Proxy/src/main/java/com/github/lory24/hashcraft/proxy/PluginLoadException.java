package com.github.lory24.hashcraft.proxy;

/**
 * This excepiton will be thrown when an error occours during plugin's loading process.
 */
public class PluginLoadException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public PluginLoadException(String message) {
        super(message);
    }
}
