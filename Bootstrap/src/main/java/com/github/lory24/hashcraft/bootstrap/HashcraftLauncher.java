package com.github.lory24.hashcraft.bootstrap;

import com.github.lory24.hashcraft.proxy.HashcraftProxyStarter;

/**
 * The HashcraftLauncher is the class that will start the Proxy software. The major feature of this class is to
 * check for additional parameters and to verify that the user is using the required version (for now it's Java 18).
 * If the program fails to run, it will print the error in the console.
 */
public class HashcraftLauncher {

    /**
     * The main function of the launcher. This will do all the stuff explained in the documentation of the class.
     * @param args The arguments of the "java -jar" command
     */
    public static void main(String[] args) {

        // Check the Java version
        if (Float.parseFloat(System.getProperty("java.class.version")) < 62.0f) {
            System.out.println("[LAUNCH-ERROR] You have to run this program with Java 18 or above. Please check your installation and try again.");
            return;
        }

        // Launch the server in a new thread with a custom name
        HashcraftProxyStarter.startHashcraftProxy();
    }
}
