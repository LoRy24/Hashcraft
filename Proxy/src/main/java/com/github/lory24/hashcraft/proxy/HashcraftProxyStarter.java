package com.github.lory24.hashcraft.proxy;

import com.github.lory24.hashcraft.proxy.logger.CustomLoggerPrintStream;
import com.github.lory24.hashcraft.proxy.logger.HashcraftLogger;

import java.io.*;
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
     * The custom logger printstream object
     */
    private static CustomLoggerPrintStream customLoggerPrintStream;

    /**
     * The hashcraft logger
     */
    private static HashcraftLogger hashcraftLogger;

    /**
     * Load the configuration and start the Hashcraft proxy. This function is called from the Bootstrap's main class.
     */
    public static void startHashcraftProxy() {

        // Set up the logger and create the folders that aren't created and load all the files. If an error occours11
        // it will stop the server
        if (!setupLogger() || !setupFoldersAndFiles()) {
            System.out.println("Error while setting up the server! Closing the software");
            return;
        }

        // Add the shutdown task
        Runtime.getRuntime().addShutdownHook(
                new Thread(HashcraftProxyStarter::onShutdown));

        // Run the server in a new thread
        Thread serverThread = new Thread(new Hashcraft(customLoggerPrintStream, hashcraftLogger)::start);
        serverThread.setName("Proxy-Thread");
        serverThread.start();
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
                FileWriter configFileWriter = new FileWriter(configFile);
                configFileWriter.write(new String(Objects.requireNonNull(HashcraftProxyStarter.class.getClassLoader().getResourceAsStream("config.yml")).readAllBytes()));
                configFileWriter.flush(); configFileWriter.close();
            } catch (IOException e) {
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
            customLoggerPrintStream = new CustomLoggerPrintStream(System.out);
            System.setOut(customLoggerPrintStream); System.setErr(customLoggerPrintStream);
            hashcraftLogger = new HashcraftLogger(HashcraftLogger.generateLoggerLogFile(), customLoggerPrintStream);

            // Create the saving loop thread
            final Thread hashcraftLoggerSavingLoopThread = new Thread(() -> {
                while (true) {
                    try {
                        hashcraftLogger.saveLogger();
                        Thread.sleep(10000);
                    } catch (InterruptedException ignored) {}
                }
            });
            hashcraftLoggerSavingLoopThread.setName("Hashcraft-Logger-SavingLoopThread");
            hashcraftLoggerSavingLoopThread.start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This function will be executed when the software closes. It will also save the logs before the software loses them.
     */
    private static void onShutdown() {
        hashcraftLogger.info("Saving and stopping the proxy server...");
        hashcraftLogger.saveLogger(); // Save the logger before closing
    }
}
