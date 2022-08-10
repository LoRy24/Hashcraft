package com.github.lory24.hashcraft.proxy.logger;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.*;

public class HashcraftLogger extends Logger {

    /**
     * The log file
     */
    @Getter
    private final File logFile;

    /**
     * The new custom printStream
     */
    @Getter
    private final CustomLoggerPrintStream customLoggerPrintStream;

    /**
     * Protected method to construct a logger for a named subsystem.
     * <p>
     * The logger will be initially configured with a null Level
     * and with useParentHandlers set to true.
     *
     * @param logFile The file where to store the log
     * @param customLoggerPrintStream The custom printstream. Used to get the buffer that store all the unsaved logs
     */
    public HashcraftLogger(final File logFile, final CustomLoggerPrintStream customLoggerPrintStream) {
        super("Hashcraft", null);
        this.logFile = logFile;
        this.customLoggerPrintStream = customLoggerPrintStream;

        // Add the handler
        addHandler(new ConsoleHandler());

        // Create a custom formatter
        Formatter customFormatter = new Formatter() {
            @SuppressWarnings("deprecation")
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
    }

    /**
     * This function will save the logs into the log file. It will take the logs from the customLoggerPrintStream
     */
    public void saveLogger() {
        try {
            Files.writeString(Path.of(this.logFile.toURI()), customLoggerPrintStream.getBuffer().toString(), StandardOpenOption.WRITE);
        } catch (IOException e) {
            this.severe("Error while storing the log. Error: " + e.getMessage());
        }
    }

    /**
     * This utility (static) function will create the log file that will store all the logs of this program. The new file
     * has as name the date of when it was created, and it will be created in the "logs" folder.
     *
     * @return The new file
     * @throws IOException If there is a problem when creating the file
     */
    @SuppressWarnings({"ResultOfMethodCallIgnored", "deprecation", "UnusedReturnValue"})
    public static @NotNull File generateLoggerLogFile() throws IOException {
        // Create the folder file object
        final File logsFolder = new File("logs");
        if (!logsFolder.exists()) logsFolder.mkdir();
        // Create the log file
        Date date = new Date(); // used to create the file
        final File logFile = new File("./logs", String.format("LOG_%d-%02.0f-%d # %02.0f-%02.0f-%02.0f.txt", date.getDate(),
                (float) date.getMonth() + 1, date.getYear() + 1900, (float) date.getHours(), (float) date.getMinutes(),
                (float) date.getSeconds()));
        logFile.createNewFile();
        return logFile;
    }
}
