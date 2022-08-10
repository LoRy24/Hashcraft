package com.github.lory24.hashcraft.proxy;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.*;

public class HashcraftLogger extends Logger {

    /**
     * Protected method to construct a logger for a named subsystem.
     * <p>
     * The logger will be initially configured with a null Level
     * and with useParentHandlers set to true.
     */
    protected HashcraftLogger(@NotNull final File log) {
        super("Hashcraft", null);

        // Set up the logger
        try {
            // Add the handlers
            addHandler(new FileHandler(log.getAbsolutePath()));
            addHandler(new ConsoleHandler());

            // Create a custom formatter
            Formatter customFormatter = new Formatter() {
                @Override
                public String format(@NotNull LogRecord record) {
                    Date date = new Date(); // Current date
                    // Return the logger format
                    return String.format("[%s-%s-%s %s:%s:%s] [Hashcraft %s] %s\n", date.getMonth() + 1, date.getDate(), date.getYear() + 1900,
                            date.getHours(), date.getMinutes(), date.getSeconds(), record.getLevel(), record.getMessage());
                }
            };

            // Apply the formatter
            Arrays.stream(getHandlers()).forEach(handler ->
                    handler.setFormatter(customFormatter)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
