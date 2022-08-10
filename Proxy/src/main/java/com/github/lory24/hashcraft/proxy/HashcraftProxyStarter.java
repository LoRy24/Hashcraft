package com.github.lory24.hashcraft.proxy;

import com.github.lory24.hashcraft.proxy.logger.CustomLoggerPrintStream;
import com.github.lory24.hashcraft.proxy.logger.HashcraftLogger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Objects;

/**
 * This class will set up all the proxy configuration, and then it will load the effective netty server. If any error
 * occurs, it will immediately kill the main thread and then the server will be stopped. The function to start the server
 * is the {@code startHashcraftProxy()} one
 */
@SuppressWarnings({"SpellCheckingInspection", "unused"})
public final class HashcraftProxyStarter {

    /**
     * Load the configuration and start the Hashcraft proxy. This function is called from the Bootstrap's main class.
     */
    public static void startHashcraftProxy() {

        // Set up the logger and create the folders that aren't created and load all the files. If an error occours
        // it will stop the server
        if (!setupLogger() || !setupFoldersAndFiles()) {
            System.out.println("Error while setting up the server! Closing the software");
        }

        // Run the server in a new thread
    }

    /**
     * Setup all the files in the server folder
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static boolean setupFoldersAndFiles() {
        String[] foldersNames = {"libs", "plugins"}; // The folders name
        // Create if it doesn't exist
        Arrays.stream(foldersNames).filter(s -> !(new File("./" + s).exists())).forEach(s -> new File("./" + s).mkdir());

        // Create the config file
        configFile: {
            try {
                // Create the file if not exists and load the configuration from the file stored in the resources
                File configFile = new File("./config.yml");
                if (configFile.exists()) break configFile;
                configFile.createNewFile();
                Files.copy(new File(Objects.requireNonNull(HashcraftProxyStarter.class.getClassLoader().getResource("config.yml"))
                        .toURI()).toPath(), configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true; // Success
    }

    /**
     * This internal function will set up the logger that will provide support when printing into the console. It will
     * also implement the custom printstream used to catch also not programmed errors.
     */
    @SuppressWarnings("BusyWait")
    private static boolean setupLogger() {
        try {
            // Load the custom print stream
            final CustomLoggerPrintStream customLoggerPrintStream = new CustomLoggerPrintStream(System.out);
            final HashcraftLogger hashcraftLogger = new HashcraftLogger(HashcraftLogger.generateLoggerLogFile(), customLoggerPrintStream);

            // Create the saving loop thread
            Thread hashcraftLoggerSavingLoopThread = new Thread(() -> {
                while (true) {
                    try {
                        hashcraftLogger.saveLogger();
                        Thread.sleep(10000);
                    } catch (InterruptedException ignored) {}
                }
            });
            hashcraftLoggerSavingLoopThread.setName("Hashcraft-Logger-SavingLoopThread");
            hashcraftLoggerSavingLoopThread.start();

            // Set the error printStream
            System.setErr(customLoggerPrintStream);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
