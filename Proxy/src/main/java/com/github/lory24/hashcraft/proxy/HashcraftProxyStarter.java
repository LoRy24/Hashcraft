package com.github.lory24.hashcraft.proxy;

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
        // Create the folder that aren't created and load all the files. If an error occours it will disable the server
        if (!setupFoldersAndFiles()) {
            System.out.println("Stopping server: Error while creating all the files");
        }

        // Run the server in a new thread
    }

    /**
     * Setup all the files in the server folder
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static boolean setupFoldersAndFiles() {
        String[] foldersNames = {"libs", "plugins", "logs"}; // The folders name
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
}
